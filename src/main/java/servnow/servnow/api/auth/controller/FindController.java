package servnow.servnow.api.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import servnow.servnow.api.auth.service.FindInfoService;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.api.dto.login.UserChangePwRequest;
import servnow.servnow.api.result.service.ResultQueryService;
import servnow.servnow.api.survey.service.SurveyQueryService;
import servnow.servnow.api.user.dto.request.CertificationNumberRequest;
import servnow.servnow.api.user.dto.request.EmailDuplicateRequest;
import servnow.servnow.api.user.service.EmailService;
import servnow.servnow.api.user.service.UserCommandService;
import servnow.servnow.api.user.service.UserQueryService;
import servnow.servnow.auth.UserId;
import servnow.servnow.common.code.CommonSuccessCode;
import servnow.servnow.common.code.LoginErrorCode;
import servnow.servnow.common.code.UserErrorCode;
import servnow.servnow.domain.user.model.UserInfo;
import servnow.servnow.domain.user.repository.UserInfoRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FindController {
    private final FindInfoService findInfoService;

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final UserInfoRepository userInfoRepository;

    String serialId = null;
    Boolean check = false;

    /**
     * db에 있는 email에 인증번호 전송
     */
    @PostMapping("/find/email-verify")
    public ServnowResponse<String> verifyEmail(@RequestBody EmailDuplicateRequest request) throws Exception {
        serialId = findInfoService.checkEmail(request.email());
        return ServnowResponse.success(CommonSuccessCode.OK);
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
    public ServnowResponse<String> changePw(@RequestBody UserChangePwRequest request) throws Exception {
        assert request.password() != null;
        if (check) {
            if (request.password().equals(request.repassword())) {
                String response = findInfoService.updatePassword(serialId, request.password());

                return ServnowResponse.success(CommonSuccessCode.OK, response);
            } else {
                return ServnowResponse.fail(LoginErrorCode.PASSWORDS_DO_NOT_MATCH);
            }
        } else {
            return ServnowResponse.fail(UserErrorCode.AUTHENTICATION_FAILED);
        }
    }
}
