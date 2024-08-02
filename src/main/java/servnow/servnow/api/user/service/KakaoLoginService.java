package servnow.servnow.api.user.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import servnow.servnow.api.auth.controller.KakaoAuthApi;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class KakaoLoginService {
    private final KakaoAuthApi kakaoAuthApi;
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

    public Map<String, Object> getUserInfo(String accessToken) {
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
            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();
            String gender = kakaoAccount.getAsJsonObject().get("gender").getAsString();
            String birthyear = kakaoAccount.getAsJsonObject().get("birthyear").getAsString();
            String birthday = kakaoAccount.getAsJsonObject().get("birthday").getAsString();

            userInfo.put("serialId", serialId);
            userInfo.put("nickname", nickname);
            userInfo.put("email", email);
            userInfo.put("gender", gender);
            userInfo.put("birth", birthyear + birthday);
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    public User saveUser(Map<String, Object> userInfo) {
        String serialId = (String) userInfo.get("serialId");
        Optional<User> existingUser = userRepository.findBySerialId(serialId);

        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        User user = User.builder()
                .serialId((String) userInfo.get("serialId"))
                .platform(userInfo.containsKey("platform") ? (Platform) userInfo.get("platform") : Platform.KAKAO)
                .status(userInfo.containsKey("status") ? (UserStatus) userInfo.get("status") : UserStatus.ACTIVE)
                .userRole(userInfo.containsKey("user_role") ? (UserRole) userInfo.get("user_role") : UserRole.USER)
                .build();

        return userRepository.save(user);
    }

    public UserInfo saveUserInfo(Map<String, Object> userInfo) {
        UserInfo userInfoEntity = UserInfo.builder()
                .email((String) userInfo.get("email"))
                .nickname((String) userInfo.get("nickname"))
                .gender((Gender) userInfo.get("gender"))
                .point(userInfo.containsKey("point") ? (Integer) userInfo.get("point") : 0)
                .level(userInfo.containsKey("level") ? (Level) userInfo.get("level") : Level.COMMONER)
                .build();

        return userInfoRepository.save(userInfoEntity);
    }

}
