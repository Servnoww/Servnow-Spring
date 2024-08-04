package servnow.servnow.api.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import servnow.servnow.api.auth.controller.KakaoAuthApi;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.api.dto.auth.KakaoToken;
import servnow.servnow.common.code.CommonSuccessCode;
import servnow.servnow.domain.user.model.User;
import servnow.servnow.domain.user.model.UserInfo;
import servnow.servnow.domain.user.model.enums.*;
import servnow.servnow.domain.user.repository.UserInfoRepository;
import servnow.servnow.domain.user.repository.UserRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class KakaoAuthService {
    private final KakaoAuthApi kakaoAuthApi;

    public String getAccessToken(String code) throws IOException {
        String reqURL = "https://kauth.kakao.com/oauth/token";

        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=authorization_code")
                .append("&client_id=").append(kakaoAuthApi.getKakaoApiKey())
                .append("&redirect_uri=").append(kakaoAuthApi.getKakaoRedirectUri())
                .append("&code=").append(code);

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()))) {
            bw.write(sb.toString());
            bw.flush();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String result;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                result = response.toString();
            }

            JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
            String accessToken = jsonObject.get("access_token").getAsString();
            String refreshToken = jsonObject.get("refresh_token").getAsString();

            UserInfo.builder().refreshToken(refreshToken);

            System.out.println("accessToken : " + accessToken);
            System.out.println("refreshToken : " + refreshToken);

            return accessToken;
        } else {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }
        }
        return reqURL;
    }

    public ServnowResponse<UserInfo> getUserInfo(String accessToken) {
        Map<String, Object> userInfo = new HashMap<>();
        String reqUrl = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            int responseCode = conn.getResponseCode();

            BufferedReader br;
            if (responseCode >= 200 && responseCode <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder responseSb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                responseSb.append(line);
            }
            String result = responseSb.toString();

            JsonElement element = JsonParser.parseString(result);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
            JsonObject profile = kakaoAccount.getAsJsonObject().get("profile").getAsJsonObject();

            String serialId = element.getAsJsonObject().get("id").getAsString();
            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String profileImage = profile.getAsJsonObject().get("profile_image_url").getAsString();
            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();
            String gender = kakaoAccount.getAsJsonObject().get("gender").getAsString();
            String birthyear = kakaoAccount.getAsJsonObject().get("birthyear").getAsString();
            String birthday = kakaoAccount.getAsJsonObject().get("birthday").getAsString();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate birthDate = LocalDate.parse(birthyear + birthday, formatter);

            User userDto = User.builder()
                    .serialId(serialId)
                    .platform(Platform.KAKAO)
                    .status(UserStatus.ACTIVE)
                    .userRole(UserRole.USER)
                    .build();

            UserInfo userInfoDto = UserInfo.builder()
                    .user(userDto)
                    .email(email)
                    .nickname(nickname)
                    .birth(birthDate)
                    .profile_url(profileImage)
                    .gender(Gender.valueOf(gender.toUpperCase()))
                    .point(0) // 기본 값 설정
                    .level(Level.COMMONER) // 기본 값 설정
                    .build();

            br.close();
            return ServnowResponse.success(CommonSuccessCode.OK, userInfoDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

        // responseBody에 있는 정보를 꺼냄
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long id = jsonNode.get("id").asLong();
        System.out.println("반환된 id: "+id);
    }
}