package servnow.servnow.api.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import servnow.servnow.api.dto.login.UserLoginResponse;
import servnow.servnow.api.user.service.UserFinder;
import servnow.servnow.api.user.service.UserInfoFinder;
import servnow.servnow.auth.jwt.JwtProvider;
import servnow.servnow.auth.jwt.Token;
import servnow.servnow.domain.user.model.User;
import servnow.servnow.domain.user.model.UserInfo;
import servnow.servnow.domain.user.model.enums.Platform;
import servnow.servnow.domain.user.model.enums.UserStatus;
import servnow.servnow.domain.user.repository.UserInfoRepository;
import servnow.servnow.domain.user.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static servnow.servnow.domain.user.model.User.createUser;
import static servnow.servnow.domain.user.model.enums.UserStatus.ACTIVE;

@Service
@AllArgsConstructor
public class KakaoService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final JwtProvider jwtProvider;
    private final UserFinder userFinder;
    private final UserInfoFinder userInfoFinder;

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

        User user = userFinder.findUserByPlatFormAndSeralId(platform, serialId)
                .orElseGet(() -> createUserAndSave(platform, serialId, nickname, gender, email, birthDate, url));

        Token issuedToken = generateTokens(user.getId());

        return UserLoginResponse.of(issuedToken, isRegistered);
    }

    private User createUserAndSave(Platform platform, String serialId, String nickname, String gender, String email, LocalDate birthDate, String url) {
        User newUser = createUser(serialId, platform);
        newUser.updateStatus(UserStatus.ACTIVE);
        userRepository.save(newUser);

        UserInfo newUserInfo = UserInfo.createMemberInfo(newUser, nickname, gender, email, birthDate, null, url);
        userInfoRepository.save(newUserInfo);

        return newUser;
    }

    private Token generateTokens(final Long id) {
        Token issuedTokens = jwtProvider.issueTokens(id, getUserRole(id));
        UserInfo userInfo = userInfoFinder.findByUserId(id);
        userInfo.setRefreshToken(issuedTokens.refreshToken());
        userInfoRepository.save(userInfo); // Ensure refresh token is saved
        return issuedTokens;
    }

    private User loadOrCreateUser(final Platform platform, final String serialId, final boolean isRegistered) {
         return userFinder.findUserByPlatFormAndSeralId(platform, serialId)
                .map(user -> updateOrFindUserInfo(user, isRegistered))
                .orElseGet(() -> {
                    User newUser = createUser(
                            serialId,
                            platform);
                    saveUser(newUser, null, null, null, null, null);
                    return newUser;
                });
    }

    private User updateOrFindUserInfo(final User user, final boolean isRegistered) {
        if (isRegistered) {
            return user;
        } else {
            return updateUserInfo(user);
        }
    }

    private User updateUserInfo(final User user) {
        user.updateStatus(ACTIVE);
        user.updateDeletedAt(null);
        return user;
    }

    private String getRefreshToken(final Long userId) {
        return userInfoFinder.findByUserId(userId).getRefreshToken();
    }

    private void saveUser(User user, String nickname, String gender, String email, LocalDate birthDate, String url) {
        userRepository.save(user);

        UserInfo newUserInfo = UserInfo.createMemberInfo(
                user, nickname, gender, email, birthDate, null, url);
        userInfoRepository.save(newUserInfo);
    }

    public HashMap<String, Object> getUserInfo(String accessToken) throws JsonProcessingException {
        HashMap<String, Object> userInfo = new HashMap<>();

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
        System.out.println("로그아웃 성공 (serialId): " + id);
    }
}
