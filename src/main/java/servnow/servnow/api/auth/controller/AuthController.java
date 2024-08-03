package servnow.servnow.api.auth.controller;

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
    public ServnowResponse<UserInfo> kakaoLogin(@RequestParam(value = "code", required = false) String code) {
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

            return ServnowResponse.success(CommonSuccessCode.OK, userInfo);
        } catch (Exception e) {
            return ServnowResponse.fail(AuthErrorCode.UNEXPECTED_ERROR);
        }
    }

    /**
     * 카카오 토큰 갱신 API
     * [GET] /app/login/kakao/:userId
     * @return BaseResponse<String>
     */
//    @ResponseBody
//    @GetMapping("/kakao/{userId}")
//    public ServnowResponse<String> updateKakaoToken(@PathVariable int userId) {
//        String result = "";
//
//        try {
//            //jwt에서 id 추출.
//            int userIdxByJwt = jwtService.getUserIdx();
//            //userIdx와 접근한 유저가 같은지 확인
//            if(userId != userIdxByJwt){
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
//						//같으면 토큰 갱신
//            loginService.updateKakaoToken(userId);
//
//            return new BaseResponse<>(result);
//        } catch (BaseException exception) {
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }
}
