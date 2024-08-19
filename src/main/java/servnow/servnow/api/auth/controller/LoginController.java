package servnow.servnow.api.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Parameter;
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
import servnow.servnow.api.user.dto.request.CertificationNumberRequest;
import servnow.servnow.api.user.dto.request.EmailDuplicateRequest;
import servnow.servnow.api.user.service.EmailService;
import servnow.servnow.api.user.service.UserQueryService;
import servnow.servnow.auth.UserId;
import servnow.servnow.auth.jwt.Token;
import servnow.servnow.common.code.CommonSuccessCode;
import servnow.servnow.common.code.UserErrorCode;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LoginController {

    private final KakaoService kakaoService;
    private final LoginService loginService;
    private final UserQueryService userQueryService;

    // 카카오 로그인
    @PostMapping("/auth/kakao")
    public ServnowResponse<UserLoginResponse> kakaoLogin(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String accessToken) throws Exception {
        String token = accessToken.startsWith("Bearer ") ? accessToken.substring(7) : accessToken;
        final UserLoginResponse response = kakaoService.login(token, "KAKAO");
        return ServnowResponse.success(CommonSuccessCode.OK, response);
    }

    @PatchMapping("/auth/logout")
    public ServnowResponse<Void> logout(@Parameter(hidden = true) @UserId Long userId) {
      loginService.logout(userId);
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
	public ServnowResponse<String> join(@RequestBody UserJoinRequest request) throws Exception {
         if (request.email() != null && !request.email().isEmpty() &&
                request.certificationNumber() != null && request.certificationNumber().equals(EmailService.ePw)) {
        		loginService.join(request);
	        	return ServnowResponse.success(CommonSuccessCode.OK, "회원가입이 완료되었습니다.");
        } else {
            return ServnowResponse.fail(UserErrorCode.CERTIFICATION_NUMBER_MISMATCH);
        }
	}

    // 회원가입 - 이메일 인증
    @PostMapping("/auth/join/identity-verification")
    public ServnowResponse<Void> identityVerification(@RequestBody UserJoinRequest request) throws Exception {
        return userQueryService.identityVerification(request.email());
    }

  @PostMapping("/auth/reissue")
  public ServnowResponse<ReissueTokenResponse> reissue(@RequestHeader("Authorization") String refreshToken) {
    return ServnowResponse.success(CommonSuccessCode.OK, loginService.reissue(refreshToken));
  }
}