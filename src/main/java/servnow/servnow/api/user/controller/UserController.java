package servnow.servnow.api.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import servnow.servnow.api.user.service.UserCommandService;
import servnow.servnow.api.user.service.UserQueryService;
import org.springframework.web.bind.annotation.*;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.api.user.controller.request.CertificationNumberRequest;
import servnow.servnow.api.user.controller.request.EmailDuplicateRequest;
import servnow.servnow.api.user.service.EmailService;
import servnow.servnow.api.user.service.response.EditProfilePageResponse;
import servnow.servnow.api.user.service.response.MyPageResponse;
import servnow.servnow.common.code.CommonSuccessCode;
import servnow.servnow.common.code.UserMyPageErrorCode;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final EmailService emailService;


    // 아직 유저 정보를 넘기는 방식이 정해지지 않아서 UserQueryService에서 userId값을 고정하여 테스트 함
    @GetMapping("/users/me")
    public ServnowResponse<MyPageResponse> getMyPage() {
        return ServnowResponse.success(CommonSuccessCode.OK, userQueryService.getMyPage());
    }

    @GetMapping("/users/me/info")
    public ServnowResponse<EditProfilePageResponse> getEditProfilePage() {
        // userId를 추출했다고 가정
        return ServnowResponse.success(CommonSuccessCode.OK, userQueryService.getEditProfilePage());
    }

    @PostMapping("/users/me/info/identity-verification")
    public ServnowResponse<Void> identityVerification(@RequestBody EmailDuplicateRequest request) {

        if (userQueryService.emailDuplicate(request.email())) {
            return ServnowResponse.fail(UserMyPageErrorCode.EMAIL_DUPLICATE);
        }
        String confirm;
        try {
            confirm = emailService.sendSimpleMessage(request.email());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (confirm.isEmpty()) {
            return ServnowResponse.fail(UserMyPageErrorCode.SEND_CERTIFICATION_NUMBER);
        } else {
            return ServnowResponse.success(CommonSuccessCode.OK);
        }
    }

    @PostMapping("/users/me/info/certification")
    public ServnowResponse<Object> CertificationNumber(@RequestBody CertificationNumberRequest request) {
        if (request.certificationNumber().equals(EmailService.ePw)) {
            return ServnowResponse.success(CommonSuccessCode.OK);
        } else {
            return ServnowResponse.fail(UserMyPageErrorCode.CERTIFICATION_NUMBER_MISMATCH);
        }
    }

//    @PostMapping("/users/me/info/save")
//    public ServnowResponse<SaveEditProfilePageRequest> save(@RequestBody final SaveEditProfilePageRequest request) {
//
//    }

}
