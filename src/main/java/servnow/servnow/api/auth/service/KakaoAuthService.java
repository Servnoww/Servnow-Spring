package servnow.servnow.api.auth.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
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
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

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

            String serialId = element.getAsJsonObject().get("id").getAsString();
            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
//            String profileImage = kakaoAccount.getAsJsonObject().get("profile_image").getAsString();
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
//                    .profile_url(profileImage)
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

//    public void updateKakaoToken(int userId) {
//        KakaoToken kakaoToken = loginProvider.getKakaoToken(userId);
//        String postURL = "https://kauth.kakao.com/oauth/token";
//        KakaoToken newToken = null;
//
//        try {
//            URL url = new URL(postURL);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setDoOutput(true);
//
//            // POST 요청에 필요한 파라미터를 OutputStream을 통해 전송
//            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
//            String sb = "grant_type=refresh_token" +
//                    "&client_id=REST_API_KEY 입력" + // REST_API_KEY
//                    "&refresh_token=" + kakaoToken.getRefresh_token() + // REFRESH_TOKEN
//                    "&client_secret=시크릿 키 입력";
//            bufferedWriter.write(sb);
//            bufferedWriter.flush();
//
//            // 요청을 통해 얻은 데이터를 InputStreamReader을 통해 읽어 오기
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line = "";
//            StringBuilder result = new StringBuilder();
//
//            while ((line = bufferedReader.readLine()) != null) {
//                result.append(line);
//            }
//            System.out.println("response body : " + result);
//
//            JsonElement element = JsonParser.parseString(result.toString());
//
//            Set<String> keySet = element.getAsJsonObject().keySet();
//
//						// 새로 발급 받은 accessToken 불러오기
//            String accessToken = element.getAsJsonObject().get("access_token").getAsString();
//	          // refreshToken은 유효 기간이 1개월 미만인 경우에만 갱신되어 반환되므로,
//						// 반환되지 않는 경우의 상황을 if문으로 처리해주었다.
//						String refreshToken = "";
//            if(keySet.contains("refresh_token")) {
//                refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();
//            }
//
//            if(refreshToken.equals("")) {
//                newToken = new KakaoToken(accessToken, kakaoToken.getRefresh_token());
//            } else {
//                newToken = new KakaoToken(accessToken, refreshToken);
//            }
//
//            bufferedReader.close();
//            bufferedWriter.close();
//
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }
//
//        try{
//            int result = 0;
//            if (newToken != null) {
//                result = loginDao.updateKakaoToken(userId, newToken);
//            }
//            if(result == 0){
//                throw new BaseException(UPDATE_FAIL_TOKEN);
//            }
//        } catch(Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//
//    }
}
