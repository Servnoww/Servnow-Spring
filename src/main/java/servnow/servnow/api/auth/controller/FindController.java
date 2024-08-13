package servnow.servnow.api.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import servnow.servnow.api.auth.service.FindInfoService;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.api.dto.login.UserChangePwRequest;
import servnow.servnow.api.user.dto.request.CertificationNumberRequest;
import servnow.servnow.api.user.dto.request.EmailDuplicateRequest;
import servnow.servnow.api.user.service.EmailService;
import servnow.servnow.api.user.service.UserCommandService;
import servnow.servnow.api.user.service.UserQueryService;
import servnow.servnow.common.code.CommonSuccessCode;
import servnow.servnow.common.code.LoginErrorCode;
import servnow.servnow.common.code.UserErrorCode;
import servnow.servnow.domain.user.model.User;
import servnow.servnow.domain.user.model.enums.Platform;
import servnow.servnow.domain.user.repository.UserInfoRepository;
import servnow.servnow.domain.user.repository.UserRepository;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FindController {
    private final FindInfoService findInfoService;

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final UserRepository userRepository;

    String serialId = null;
    Boolean check = false;

    /**
     * db에 있는 email에 인증번호 전송
     */
    @PostMapping("/find/email-verify")
    public ServnowResponse<String> verifyEmail(@RequestBody EmailDuplicateRequest request) throws Exception {
        serialId = findInfoService.checkEmail(request.email());

        if (serialId == null) {
            return ServnowResponse.fail(LoginErrorCode.USER_NOT_FOUND);
        }

        Optional<User> userOptional = userRepository.findBySerialId(serialId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getPlatform().equals(Platform.KAKAO)) {
                return ServnowResponse.fail(LoginErrorCode.INVALID_PW_KAKAO_USER);
            }

            return ServnowResponse.success(CommonSuccessCode.OK);
        } else {
            return ServnowResponse.fail(LoginErrorCode.USER_NOT_FOUND);
        }
    }

    @PostMapping("/find/id")
    public ServnowResponse<String> findId(@RequestBody CertificationNumberRequest request) {
        if (request.certificationNumber().equals(EmailService.ePw)) {
            return ServnowResponse.success(CommonSuccessCode.OK, serialId);
        } else {
            return ServnowResponse.fail(UserErrorCode.CERTIFICATION_NUMBER_MISMATCH);
        }
    }

    @PostMapping("/find/pw")
    public ServnowResponse<Boolean> findPw(@RequestBody CertificationNumberRequest request) {
        if (request.certificationNumber().equals(EmailService.ePw)) {
            check = true;
            return ServnowResponse.success(CommonSuccessCode.OK);
        } else {
            check = false;
            return ServnowResponse.fail(UserErrorCode.CERTIFICATION_NUMBER_MISMATCH);
        }
    }

    @PostMapping("/change/pw")
    public ServnowResponse<Object> changePw(@RequestBody UserChangePwRequest request) throws Exception {
        assert request.password() != null;

        // 이메일 인증이 되었다면
        if (check) {
            return findInfoService.updatePassword(serialId, request.password(), request.repassword());
        } else {
            return ServnowResponse.fail(UserErrorCode.AUTHENTICATION_FAILED);
        }
    }
}
