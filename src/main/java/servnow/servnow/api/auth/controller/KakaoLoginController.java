package servnow.servnow.api.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import servnow.servnow.api.auth.service.KakaoService;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.api.dto.login.UserLoginRequest;
import servnow.servnow.api.dto.login.UserLoginResponse;
import servnow.servnow.common.code.CommonSuccessCode;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class KakaoLoginController {

    private final KakaoService kakaoService;

    @PostMapping("/auth/kakao")
    public ServnowResponse<UserLoginResponse> login(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String accessToken,
            @RequestParam String name,
            @RequestParam String platform) throws IOException {
        String token = accessToken.startsWith("Bearer ") ? accessToken.substring(7) : accessToken;
        UserLoginRequest request = new UserLoginRequest(name, platform);
        final UserLoginResponse response = kakaoService.login(token, request);

        return ServnowResponse.success(CommonSuccessCode.OK, response);
    }

    @PostMapping("/auth/kakao/logout")
    public ServnowResponse kakaoLogout(@RequestHeader("Authorization") String accessToken) throws JsonProcessingException {
        String token = accessToken.replace("Bearer ", "");
        kakaoService.kakaoDisconnect(token);

        return ServnowResponse.success(CommonSuccessCode.OK);
    }
}