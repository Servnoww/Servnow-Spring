package servnow.servnow.api.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import servnow.servnow.api.auth.service.AuthService;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.common.code.AuthErrorCode;
import servnow.servnow.common.code.CommonSuccessCode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;


    /***
     *
     * @param code
     * @return
     */
    @GetMapping("/kakao")
    public ServnowResponse<String> kakaoLogin(@RequestParam(value = "code", required = false) String code) {
        if (code == null || code.isEmpty()) {
            return ServnowResponse.fail(AuthErrorCode.INVALID_REQUEST);
        }

        try {
            // access token 발급
            String accessToken = authService.getAccessToken(code);

            // 사용자 정보 가져오기
            Map<String, Object> userInfo = authService.getUserInfo(accessToken);

            String email = (String)userInfo.get("email");
            String nickname = (String)userInfo.get("nickname");
//            String gender = (String)userInfo.get("gender");

//            System.out.println("email = " + email);
//            System.out.println("nickname = " + nickname);
//            System.out.println("accessToken = " + accessToken);
            Map<String, String> data = new HashMap<>();
            data.put("email", email);
            data.put("nickname", nickname);

            return ServnowResponse.success(CommonSuccessCode.OK, data.toString());
        } catch (Exception e) {
            return ServnowResponse.fail(AuthErrorCode.UNEXPECTED_ERROR);
        }
    }
}
