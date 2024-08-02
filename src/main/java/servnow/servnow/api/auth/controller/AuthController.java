package servnow.servnow.api.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import servnow.servnow.api.auth.service.KakaoAuthService;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.common.code.AuthErrorCode;
import servnow.servnow.common.code.CommonSuccessCode;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final KakaoAuthService kakaoAuthService;

    /***
     * 카카오 로그인 API
     * @param code
     * @return data: 회원 정보
     */
    @GetMapping("/kakao")
    public ServnowResponse<String> kakaoLogin(@RequestParam(value = "code", required = false) String code) {
        if (code == null || code.isEmpty()) {
            return ServnowResponse.fail(AuthErrorCode.INVALID_REQUEST);
        }

        try {
            // access token 발급
            String accessToken = kakaoAuthService.getAccessToken(code);

            // 사용자 정보 가져오기
            Map<String, Object> userInfo = kakaoAuthService.getUserInfo(accessToken);

            // 사용자 정보 저장
//            authService.saveUser(userInfo);
//            authService.saveUserInfo(userInfo);

            return ServnowResponse.success(CommonSuccessCode.OK, userInfo.toString());
        } catch (Exception e) {
            return ServnowResponse.fail(AuthErrorCode.UNEXPECTED_ERROR);
        }
    }
}
