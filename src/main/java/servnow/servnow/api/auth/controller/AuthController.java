package servnow.servnow.api.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import servnow.servnow.api.auth.service.KakaoService;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.api.dto.auth.AuthToken;
import servnow.servnow.api.dto.login.LoginResponse;
import servnow.servnow.common.code.AuthErrorCode;
import servnow.servnow.common.code.CommonSuccessCode;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final KakaoService kakaoAuthService;
    private AuthToken authToken;


    /***
     * 카카오 로그인 API
     * @param code
     * @return data: 회원 정보
     */
    @ResponseBody
    @GetMapping("/kakao")
    public ServnowResponse<LoginResponse> kakaoLogin(@RequestParam(value = "code", required = false) String code) {
        try {
            // access token 발급
/*            String accessToken = kakaoAuthService.getAccessToken(code);

            // 사용자 정보 가져오기
            HashMap<String, Object> user = kakaoAuthService.getUserInfo(accessToken);

            // 사용자 정보 저장
            kakaoAuthService.kakaoUserLogin(user);*/

            return ServnowResponse.success(CommonSuccessCode.OK, kakaoAuthService.kakaoLogin(code));
        } catch (Exception e) {
            return ServnowResponse.fail(AuthErrorCode.INVALID_ACCESS_TOKEN);
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
     public ServnowResponse<String> kakaoLogout(HttpSession session) {
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
