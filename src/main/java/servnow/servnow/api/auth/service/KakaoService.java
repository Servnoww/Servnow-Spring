package servnow.servnow.api.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import servnow.servnow.api.dto.login.UserLoginRequest;
import servnow.servnow.api.dto.login.UserLoginResponse;
import servnow.servnow.api.user.service.UserFinder;
import servnow.servnow.api.user.service.UserInfoFinder;
import servnow.servnow.auth.jwt.JwtProvider;
import servnow.servnow.auth.jwt.Token;
import servnow.servnow.domain.user.model.User;
import servnow.servnow.domain.user.model.UserInfo;
import servnow.servnow.domain.user.model.enums.*;
import servnow.servnow.domain.user.repository.UserInfoRepository;
import servnow.servnow.domain.user.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static servnow.servnow.domain.user.model.User.createUser;

@Service
@AllArgsConstructor
public class KakaoService {
	private final UserRepository userRepository;
	private final UserInfoRepository userInfoRepository;

    private final JwtProvider jwtProvider;
    private UserFinder userFinder;
    private UserInfoFinder userInfoFinder;
    private UserUpdater userUpdater;

     public UserLoginResponse login(String accessToken, String pf) throws IOException {
        HashMap<String, Object> userInfo = getUserInfo(accessToken);

        String serialId = (String) userInfo.getOrDefault("id", "unknown");
        String email = (String) userInfo.getOrDefault("email", "unknown@example.com");
        String nickname = (String) userInfo.getOrDefault("nickname", "unknown");
        String gender = (String) userInfo.getOrDefault("gender", "unknown");
        String url = (String) userInfo.getOrDefault("profile_url", "default_url");
        LocalDate birthDate = (LocalDate) userInfo.getOrDefault("birthDate", LocalDate.now());

        Platform platform = Platform.getEnumPlatformFromStringPlatform(pf);
        boolean isRegistered = userFinder.isRegisteredUser(platform, serialId);

        User findUser = loadOrCreateUser(Platform.KAKAO, serialId, isRegistered);
        saveUser(findUser, nickname, gender, email, birthDate, url);

        // jwt 토큰 생성
        Token issuedToken = generateTokens(findUser.getId());

        return UserLoginResponse.of(issuedToken, isRegistered);
    }

    private Token generateTokens(final Long id) {
        Token issuedTokens = jwtProvider.issueTokens(id, getUserRole(id));
        UserInfo findInfoUser = userInfoFinder.findByUserId(id);
        findInfoUser.setRefreshToken(issuedTokens.refreshToken());
        return issuedTokens;
    }

     private User loadOrCreateUser(final Platform platform, final String serialId, final boolean isRegistered) {
        return userFinder.findUserByPlatFormAndSeralId(platform, serialId)
//                .map(user -> updateOrFindUserInfo(user, isRegistered))
                .orElseGet(() -> {
                    User newUser = createUser(
                            serialId,
                            platform);
                    saveUser(newUser, null, null, null, null, null);
                    return newUser;
                });
    }

//    private void saveUser(final User user) {
//        userUpdater.saveUser(user);
//        UserInfo userInfo = UserInfo.createMemberInfo(user, null);
//        userInfoUpdater.saveUserInfo(userInfo);
//    }

     private void saveUser(User user, String nickname, String gender, String email, LocalDate birthDate, String url) {
        userUpdater.saveUser(user);

        if (userInfoFinder == null) {
            UserInfo newUserInfo = UserInfo.createMemberInfo(
                    user, null, nickname, gender, email, birthDate, url);
            userInfoRepository.save(newUserInfo);
        } else {
//            userInfoFinder.update(nickname, gender, email, birthDate, url);
//            userInfoRepository.save(userInfoFinder);
        }
    }


    // 사용자 정보 조회
    public HashMap<String, Object> getUserInfo(String accessToken) throws JsonProcessingException {
        HashMap<String, Object> userInfo = new HashMap<>();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> kakaoUserInfoRequest = new HttpEntity<>(headers);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET, // GET 방식으로 요청
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode element = objectMapper.readTree(responseBody);

        String id = element.get("id").asText();
        String email = element.path("kakao_account").path("email").asText();
        String gender = element.path("kakao_account").path("gender").asText().toUpperCase();
        String birthyear = element.path("kakao_account").path("birthyear").asText();
        String birthday = element.path("kakao_account").path("birthday").asText();
        String nickname = element.path("properties").path("nickname").asText();
        String profileUrl = element.path("properties").path("profile_image").asText();


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate birthDate = LocalDate.parse(birthyear + birthday, formatter);

        userInfo.put("id", id);
        userInfo.put("email", email);
        userInfo.put("gender", gender);
        userInfo.put("nickname", nickname);
        userInfo.put("profile_url", profileUrl);
        userInfo.put("birthDate", birthDate);

        return userInfo;
    }


    private String getUserRole(final Long id) {
        return userFinder.findById(id).getUserRole().getValue();
    }

    private String getRefreshToken(final Long id) {
        return userInfoFinder.findByUserId(id).getRefreshToken();
    }

     public void kakaoDisconnect(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoLogoutRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/user/logout",
                HttpMethod.POST,
                kakaoLogoutRequest,
                String.class
        );

        // responseBody에 있는 정보 꺼내기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long id = jsonNode.get("id").asLong();
        System.out.println("로그아웃 성공 (serialId): "+id);
    }
}