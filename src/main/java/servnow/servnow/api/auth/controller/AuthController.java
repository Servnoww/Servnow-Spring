package servnow.servnow.api.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import servnow.servnow.api.auth.service.KakaoAuthService;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.api.user.service.KakaoLoginService;
import servnow.servnow.common.code.AuthErrorCode;
import servnow.servnow.common.code.CommonSuccessCode;
import servnow.servnow.domain.user.model.UserInfo;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final KakaoAuthService kakaoAuthService;
    private final KakaoLoginService kakaoLoginService;

    /***
     * 카카오 로그인 API
     * @param code
     * @return data: 회원 정보
     */
    @GetMapping("/kakao")
    public ServnowResponse<UserInfo> kakaoLogin(
            @RequestParam(value = "code", required = false) String code,
            HttpSession session
    ) {
        if (code == null || code.isEmpty()) {
            return ServnowResponse.fail(AuthErrorCode.INVALID_REQUEST);
        }

        try {
            // access token 발급
            String accessToken = kakaoAuthService.getAccessToken(code);

            // 사용자 정보 가져오기
            UserInfo userInfo = kakaoAuthService.getUserInfo(accessToken).getData();

            // 사용자 정보 저장
            kakaoLoginService.saveUser(userInfo);

            // 로그인
            if (userInfo != null) {
                session.setAttribute("loginMember", userInfo);
                session.setMaxInactiveInterval(60 * 30);
                session.setAttribute("kakaoToken", accessToken);
            }

            return ServnowResponse.success(CommonSuccessCode.OK, userInfo);
        } catch (Exception e) {
            return ServnowResponse.fail(AuthErrorCode.UNEXPECTED_ERROR);
        }
    }

    /**
     * 카카오 토큰 갱신 API
     * @param userInfoId
     * @return ServnowResponse<UserInfo>
     */
//    @GetMapping("/kakao/refresh/{userInfoId}")
//    public ServnowResponse<UserInfo> refreshKakaoToken(@PathVariable Long userInfoId) {
//        try {
//            // 새로운 액세스 토큰 발급
//            String newAccessToken = kakaoAuthService.refreshAccessToken(userInfoId);
//
//            // 사용자 정보 가져오기
//            ServnowResponse<UserInfo> response = kakaoAuthService.getUserInfo(newAccessToken);
//
//            // 사용자 정보 저장
//            kakaoLoginService.saveUser(response.getData());
//
//            return ServnowResponse.success(CommonSuccessCode.OK, response.getData());
//        } catch (IOException e) {
//            return ServnowResponse.fail(AuthErrorCode.UNEXPECTED_ERROR);
//        }
//    }

    /**
     * 카카오 로그아웃
     * @return
     */
     @GetMapping("/kakao/logout")
     public ServnowResponse<Object> kakaoLogout(HttpSession session) {
        String accessToken = (String) session.getAttribute("kakaoToken");

        if(accessToken != null && !"".equals(accessToken)){
            try {
                kakaoAuthService.kakaoDisconnect(accessToken);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            session.removeAttribute("kakaoToken");
            session.removeAttribute("loginMember");

            return ServnowResponse.success(CommonSuccessCode.OK);
        }else{
            System.out.println("accessToken is null");
        }

        return ServnowResponse.fail(AuthErrorCode.INTERNAL_SERVER_ERROR);
    }
}
