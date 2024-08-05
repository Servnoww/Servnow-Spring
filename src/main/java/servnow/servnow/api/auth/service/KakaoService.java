package servnow.servnow.api.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import servnow.servnow.api.auth.controller.KakaoAuthApi;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.api.dto.auth.AuthToken;
import servnow.servnow.api.dto.login.LoginResponse;
import servnow.servnow.auth.jwt.JwtGenerator;
import servnow.servnow.auth.jwt.JwtProvider;
import servnow.servnow.common.code.CommonSuccessCode;
import servnow.servnow.domain.user.model.User;
import servnow.servnow.domain.user.model.enums.*;
import servnow.servnow.domain.user.repository.UserRepository;

import java.io.IOException;
import java.util.HashMap;

@Service
@AllArgsConstructor
public class KakaoService {
    private final KakaoAuthApi kakaoAuthApi;

	private final UserRepository userRepository;
    private final JwtGenerator authTokensGenerator;
	private final JwtProvider jwtTokenProvider;

     public LoginResponse kakaoLogin(String code) throws IOException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. 토큰으로 카카오 API 호출
        HashMap<String, Object> userInfo = getUserInfo(accessToken);

        //3. 카카오ID로 회원가입 & 로그인 처리
        LoginResponse loginResponse= kakaoUserLogin(userInfo);

        return loginResponse;
    }

     private LoginResponse kakaoUserLogin(HashMap<String, Object> userInfo){
        String serialId = userInfo.get("id").toString();

        User kakaoUser = userRepository.findBySerialId(serialId).orElse(null);

        if (kakaoUser == null) {    // 회원가입
        	kakaoUser = User.builder()
        	    .serialId(serialId)
                .userRole(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .platform(Platform.KAKAO)
                .build();

            userRepository.save(kakaoUser);
        }

        // 토큰 생성
        AuthToken token = authTokensGenerator.generate(serialId);

        return new LoginResponse(serialId, token);
    }

    // 토큰 발급
    public String getAccessToken(String code) throws IOException {
        // HTTP Header 생성
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = getHttpEntity(code);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String access_token = jsonNode.get("access_token").asText();

        return access_token;
    }

    private HttpEntity<MultiValueMap<String, String>> getHttpEntity(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoAuthApi.getKakaoApiKey());
        body.add("redirect_uri", kakaoAuthApi.getKakaoRedirectUri());
        body.add("code", code);

        // HTTP 요청 보내기
        return new HttpEntity<>(body, headers);
    }

    // 사용자 정보 조회
    public HashMap<String, Object> getUserInfo(String accessToken) throws JsonProcessingException {
        HashMap<String, Object> userInfo = new HashMap<String,Object>();

         // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode element = objectMapper.readTree(responseBody);

        String serialId = element.get("id").asText();


        userInfo.put("id", serialId);

        return userInfo;
    }

     public void kakaoDisconnect(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded");

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