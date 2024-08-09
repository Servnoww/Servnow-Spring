package servnow.servnow.api.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import servnow.servnow.api.auth.service.KakaoService;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.api.dto.login.UserLoginRequest;
import servnow.servnow.api.dto.login.UserLoginResponse;
import servnow.servnow.auth.jwt.Token;
import servnow.servnow.common.code.CommonSuccessCode;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class KakaoLoginController {

    private final KakaoService kakaoService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    @ResponseBody
//    @PostMapping("/kakao")
//    public ServnowResponse<UserLoginResponse> kakaoLogin(@RequestBody String accessToken) throws IOException {
//
//        return ServnowResponse.success(CommonSuccessCode.OK, kakaoService.login(accessToken));
//    }

    @PostMapping("/auth/kakao")
    public ServnowResponse<UserLoginResponse> login(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String token,
            @RequestParam String name,
            @RequestParam String platform) throws IOException {
        String accessToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        logger.info("오오오"+accessToken);
        UserLoginRequest request = new UserLoginRequest(name, platform);
        final UserLoginResponse response = kakaoService.login(accessToken, request);

        return ServnowResponse.success(CommonSuccessCode.OK, response);
    }

}