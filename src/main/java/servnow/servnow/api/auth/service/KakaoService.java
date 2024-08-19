package servnow.servnow.api.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import servnow.servnow.api.dto.login.UserLoginResponse;
import servnow.servnow.api.user.service.UserFinder;
import servnow.servnow.api.user.service.UserInfoFinder;
import servnow.servnow.auth.jwt.JwtProvider;
import servnow.servnow.auth.jwt.Token;
import servnow.servnow.common.code.AuthErrorCode;
import servnow.servnow.domain.user.model.User;
import servnow.servnow.domain.user.model.UserInfo;
import servnow.servnow.domain.user.model.enums.Gender;
import servnow.servnow.domain.user.model.enums.Platform;
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
    private final UserFinder userFinder;
    private final UserInfoFinder userInfoFinder;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 카카오 로그인 처리
     */
    @Transactional
    public UserLoginResponse login(String accessToken, String pf) throws Exception {
        HashMap<String, Object> userInfo = getUserInfo(accessToken);

        String serialId = (String) userInfo.getOrDefault("id", "unknown");
        String email = (String) userInfo.getOrDefault("email", "unknown@example.com");
        String nickname = (String) userInfo.getOrDefault("nickname", "unknown");
        String gender = (String) userInfo.getOrDefault("gender", "unknown");
        String profileUrl = (String) userInfo.getOrDefault("profile_url", "default_url");
//        LocalDate birthDate = (LocalDate) userInfo.getOrDefault("birthDate", LocalDate.now());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String birthDate = ((LocalDate) userInfo.getOrDefault("birthDate", LocalDate.now())).format(formatter);

        Platform platform = Platform.getEnumPlatformFromStringPlatform(pf);
        boolean isRegistered = userFinder.isRegisteredUser(platform, serialId);

        Gender genderEnum = Gender.getEnumGenderFromStringGender(gender);

         User user = loadOrCreateUser(platform, serialId, isRegistered);
        if (!isRegistered) {
            saveUserInfo(user, nickname, genderEnum, email, birthDate, profileUrl);
        }

        Token issuedToken = generateTokens(user.getId());
        return UserLoginResponse.of(issuedToken, isRegistered);
    }

    private Token generateTokens(Long id) {
        Token issuedTokens = jwtProvider.issueTokens(id, getUserRole(id));
        UserInfo userInfo = userInfoFinder.findByUserId(id);
        userInfo.updateRefreshToken(issuedTokens.refreshToken());
        return issuedTokens;
    }


//    private User loadOrCreateUser(Platform platform, String serialId, boolean isRegistered) {
//        System.out.println(userFinder.findUserByPlatFormAndSeralId(platform, serialId));
//        return userFinder.findUserByPlatFormAndSeralId(platform, serialId)
//                .map(user -> updateOrFindUserInfo(user, isRegistered))
//                .orElseGet(() -> {
//                    User newUser = createUser(serialId, platform);
//                    System.out.println("hi000");
//                    return saveUser(newUser);
//                });
//    }
    private User loadOrCreateUser(Platform platform, String serialId, boolean isRegistered) throws Exception {
    try {
        // 사용자 찾기 시도
        System.out.println(userFinder.findUserByPlatFormAndSeralId(platform, serialId));
        return userFinder.findUserByPlatFormAndSeralId(platform, serialId)
                .map(user -> updateOrFindUserInfo(user, isRegistered))
                .orElseGet(() -> {
                    User newUser = createUser(serialId, platform);
                    System.out.println("hi000");
                    return saveUser(newUser);
                });
    } catch (IllegalArgumentException e) {
        // 잘못된 플랫폼 타입 예외 처리
        throw new Exception(String.valueOf(AuthErrorCode.INVALID_PLATFORM_TYPE));
    } catch (Exception e) {
        // 예상하지 못한 예외 처리
        throw new Exception(String.valueOf(AuthErrorCode.INTERNAL_SERVER_ERROR));
    }
}

    private User updateOrFindUserInfo(User user, boolean isRegistered) {
        return isRegistered ? user : saveUser(user);
    }


    private User saveUser(User user) {
        userRepository.save(user);
        return user;
    }


    private void saveUserInfo(User user, String nickname, Gender gender, String email, String birthDate, String profileUrl) {
        UserInfo newUserInfo = UserInfo.createMemberInfo(user, nickname, gender, email, birthDate, null, profileUrl);
        userInfoRepository.save(newUserInfo);
    }

    public HashMap<String, Object> getUserInfo(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
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

        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", id);
        userInfo.put("email", email);
        userInfo.put("gender", gender);
        userInfo.put("nickname", nickname);
        userInfo.put("profile_url", profileUrl);
        userInfo.put("birthDate", birthDate);

        return userInfo;
    }


    private String getUserRole(Long id) {
        return userFinder.findById(id).getUserRole().getValue();
    }

    /**
     * 카카오 계정 로그아웃 메서드
     */
    public void kakaoDisconnect(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;");

        HttpEntity<MultiValueMap<String, String>> kakaoLogoutRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/user/logout",
                HttpMethod.POST,
                kakaoLogoutRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long id = jsonNode.get("id").asLong();
        logger.info("로그아웃 성공 (serialId): " + id);
    }
}
