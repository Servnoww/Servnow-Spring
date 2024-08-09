package servnow.servnow.api.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import servnow.servnow.api.dto.login.UserLoginRequest;
import servnow.servnow.api.dto.login.UserLoginResponse;
import servnow.servnow.auth.jwt.JwtGenerator;
import servnow.servnow.auth.jwt.JwtProvider;
import servnow.servnow.auth.jwt.Token;
import servnow.servnow.domain.user.model.User;
import servnow.servnow.domain.user.model.UserInfo;
import servnow.servnow.domain.user.model.enums.*;
import servnow.servnow.domain.user.repository.UserRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

import static servnow.servnow.domain.user.model.User.createUser;
import static servnow.servnow.domain.user.model.enums.UserStatus.ACTIVE;

@Service
@AllArgsConstructor
public class KakaoService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private UserFinder userFinder;
    private UserInfoFinder userInfoFinder;
    private UserUpdater userUpdater;

     public UserLoginResponse login(String accessToken, UserLoginRequest request) throws IOException {
        HashMap<String, Object> userInfo = getUserInfo(accessToken);

        String serialId = userInfo.get("id").toString();

        Platform platform = Platform.getEnumPlatformFromStringPlatform(request.platform());
        boolean isRegistered = userFinder.isRegisteredUser(platform, serialId);

        User findUser = loadOrCreateUser(Platform.KAKAO, serialId, isRegistered);

        // jwt 토큰 생성
        Token issuedToken = generateTokens(findUser.getId());

        return UserLoginResponse.of(issuedToken, isRegistered);
    }

//     private Token userLogin(HashMap<String, Object> userInfo){
//        String id = userInfo.get("id").toString();
//
//        User kakaoUser = userRepository.findBySerialId(id).orElse(null);
//
//        if (kakaoUser == null) {    // 회원가입
//        	kakaoUser = User.builder()
//        	    .serialId(id)
//                .userRole(UserRole.USER)
//                .status(ACTIVE)
//                .platform(Platform.KAKAO)
//                .build();
//
//            userRepository.save(kakaoUser);
//        }
//
//        // jwt 토큰 생성
//        Token token = authTokensGenerator.generate(id);
//        System.out.println("AuthToken: "+token);
//
//        return token;
//    }

    private Token generateTokens(final Long id) {
        Token issuedTokens = jwtProvider.issueTokens(id, getUserRole(id));
//        UserInfo findInfoUser = userInfoFinder.getUserInfo(id);
//        findInfoUser.updateRefreshToken(issuedTokens.refreshToken());
        User findUser = userFinder.getUser(id);
        return issuedTokens;
    }

     private User loadOrCreateUser(final Platform platform, final String serialId, final boolean isRegistered) {
        return userFinder.findUserByPlatFormAndSeralId(platform, serialId)
//                .map(user -> updateOrFindUserInfo(user, isRegistered))
                .orElseGet(() -> {
                    User newUser = createUser(
                            serialId,
                            platform);
                    saveUser(newUser);
                    return newUser;
                });
    }

    private void saveUser(final User user) {
        userUpdater.saveUser(user);
//        UserInfo userInfo = UserInfo.createMemberInfo(user, null);
//        userInfoUpdater.saveUserInfo(userInfo);
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

        // 응답 본문 처리
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode element = objectMapper.readTree(responseBody);

        // 응답에서 사용자 ID 추출
        String serialId = element.get("id").asText();
        userInfo.put("id", serialId);

        System.out.println("serialId: " + serialId);

        return userInfo;
    }



    private String getUserRole(final Long id) {
        return userFinder.getUser(id).getUserRole().getValue();
    }

//    private String getRefreshToken(final String serialId) {
//        return userFinder.getUser(serialId).getRefreshToken();
//    }

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
//        System.out.println("로그아웃 성공 (serialId): "+id);
    }
}