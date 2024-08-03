package servnow.servnow.api.user.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import servnow.servnow.api.auth.controller.KakaoAuthApi;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.common.code.AuthErrorCode;
import servnow.servnow.common.code.CommonSuccessCode;
import servnow.servnow.common.code.LoginErrorCode;
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

     public ServnowResponse<Void> saveUser(UserInfo userInfoDto) {
        User userDto = userInfoDto.getUser();
        String serialId = userDto.getSerialId();
        Optional<User> existingUser = userRepository.findBySerialId(serialId);

        if (existingUser.isPresent()) {
            return ServnowResponse.fail(LoginErrorCode.ALREADY_REGISTERED);
        }

        User user = User.builder()
                .id(userDto.getId())
                .serialId(userDto.getSerialId())
                .platform(userDto.getPlatform())
                .status(userDto.getStatus())
                .userRole(userDto.getUserRole())
                .build();

        userRepository.save(user);

        UserInfo userInfo = UserInfo.builder()
                .user(user)
                .id(userInfoDto.getId())
                .email(userInfoDto.getEmail())
                .nickname(userInfoDto.getNickname())
                .gender(userInfoDto.getGender())
                .birth(userInfoDto.getBirth())
                .point(userInfoDto.getPoint())
                .level(userInfoDto.getLevel())
                .build();

        userInfoRepository.save(userInfo);

        return ServnowResponse.success(null); // 데이터가 없으므로 null로 설정
    }
}
