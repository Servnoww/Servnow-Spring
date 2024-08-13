package servnow.servnow.api.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import servnow.servnow.api.auth.dto.response.ReissueTokenResponse;
import servnow.servnow.api.auth.service.KakaoService;
import servnow.servnow.api.auth.service.LoginService;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.api.dto.login.UserJoinRequest;
import servnow.servnow.api.dto.login.UserLoginRequest;
import servnow.servnow.api.dto.login.UserLoginResponse;
import servnow.servnow.auth.jwt.Token;
import servnow.servnow.common.code.CommonSuccessCode;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LoginController {

    private final KakaoService kakaoService;
    private final LoginService loginService;

    // 카카오 로그인
    @PostMapping("/auth/kakao")
    public ServnowResponse<UserLoginResponse> kakaoLogin(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String accessToken) throws IOException {
        String token = accessToken.startsWith("Bearer ") ? accessToken.substring(7) : accessToken;
        final UserLoginResponse response = kakaoService.login(token, "KAKAO");

        return ServnowResponse.success(CommonSuccessCode.OK, response);
    }

    // 카카오 로그아웃
    @PostMapping("/auth/kakao/logout")
    public ServnowResponse kakaoLogout(@RequestHeader("Authorization") String accessToken) throws JsonProcessingException {
        String token = accessToken.replace("Bearer ", "");
        kakaoService.kakaoDisconnect(token);

        return ServnowResponse.success(CommonSuccessCode.OK);
    }

    // 일반 로그인
	@PostMapping("/auth/login")
	public ServnowResponse<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
		UserLoginResponse userLoginResponse = loginService.login(request);
		return ServnowResponse.success(CommonSuccessCode.OK, userLoginResponse);
	}

    // 일반 회원가입
    @PostMapping("/auth/join")
	public ServnowResponse<String> join(@RequestBody UserJoinRequest request) {
		loginService.join(request);
		return ServnowResponse.success(CommonSuccessCode.OK, "회원가입이 완료되었습니다.");
	}

  @PostMapping("/auth/reissue")
  public ServnowResponse<ReissueTokenResponse> reissue(@RequestHeader("Authorization") String refreshToken) {
    return ServnowResponse.success(CommonSuccessCode.OK, loginService.reissue(refreshToken));
  }
}