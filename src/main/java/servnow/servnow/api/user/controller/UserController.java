package servnow.servnow.api.user.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.api.result.dto.request.MySurveysResultMemoRequest;
import servnow.servnow.api.result.dto.response.MySurveysResultMemoResponse;
import servnow.servnow.api.result.dto.response.MySurveysResultResponse;
import servnow.servnow.api.result.service.ResultCommandService;
import servnow.servnow.api.result.service.ResultQueryService;
import servnow.servnow.api.user.dto.request.CertificationNumberRequest;
import servnow.servnow.api.user.dto.request.EmailDuplicateRequest;
import servnow.servnow.api.user.dto.request.SaveEditProfilePageRequest;
import servnow.servnow.api.user.dto.request.SerialIdDuplicateRequest;
import servnow.servnow.api.user.dto.response.EditProfilePageResponse;
import servnow.servnow.api.user.dto.response.MyPageResponse;
import servnow.servnow.api.user.dto.response.MySurveyResponse;
import servnow.servnow.api.survey.service.SurveyQueryService;
import servnow.servnow.api.user.dto.response.UserPointGetResponse;
import servnow.servnow.api.user.service.EmailService;
import servnow.servnow.api.user.service.UserCommandService;
import servnow.servnow.api.user.service.UserQueryService;
import servnow.servnow.auth.UserId;
import servnow.servnow.common.code.CommonSuccessCode;
import servnow.servnow.common.code.UserErrorCode;

import java.util.List;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final ResultQueryService resultQueryService;
    private final ResultCommandService resultCommandService;
    private final SurveyQueryService surveyQueryService;



    // 아직 유저 정보를 넘기는 방식이 정해지지 않아서 UserQueryService에서 userId값을 고정하여 테스트 함
    @GetMapping("/users/me")
    public ServnowResponse<MyPageResponse> getMyPage(@Parameter (hidden = true) @UserId final Long userId) {
        return ServnowResponse.success(CommonSuccessCode.OK, userQueryService.getMyPage(userId));
    }

    @GetMapping("/users/me/info")
    public ServnowResponse<EditProfilePageResponse> getEditProfilePage(@Parameter (hidden = true) @UserId final Long userId) {
        return ServnowResponse.success(CommonSuccessCode.OK, userQueryService.getEditProfilePage(userId));
    }

    @GetMapping("/users/me/id")
    public ServnowResponse<Boolean> getSerialIdDuplicate(@RequestBody SerialIdDuplicateRequest request) {
        return ServnowResponse.success(CommonSuccessCode.OK, userQueryService.getSerialIdDuplicate(request.serialId()));
    }

    @PostMapping("/users/me/info/identity-verification")
    public ServnowResponse<Void> identityVerification(@RequestBody EmailDuplicateRequest request) throws Exception {
        return userQueryService.identityVerification(request.email());
    }

    @PostMapping("/users/me/info/certification")
    public ServnowResponse<Object> CertificationNumber(@RequestBody CertificationNumberRequest request) {
        if (request.certificationNumber().equals(EmailService.ePw)) {
            return ServnowResponse.success(CommonSuccessCode.OK);
        } else {
            return ServnowResponse.fail(UserErrorCode.CERTIFICATION_NUMBER_MISMATCH);
        }
    }

    @PatchMapping("/users/me/info/save")
    public ServnowResponse<SaveEditProfilePageRequest> profileSave(@Parameter (hidden = true) @UserId final Long userId, @RequestBody final SaveEditProfilePageRequest request) {
        // 이메일이 변경되었고, 인증번호가 있는 경우
        if (request.email() != null && !request.email().isEmpty() &&
                request.certificationNumber() != null && request.certificationNumber().equals(EmailService.ePw)) {
            userCommandService.profileSave(userId, request);
            return ServnowResponse.success(CommonSuccessCode.OK);
        } else if ((request.certificationNumber() == null) || request.certificationNumber().isEmpty()) {
            // 인증번호 없이 아이디 또는 비밀번호만 변경하려는 경우
            userCommandService.profileSave(userId, request);
            return ServnowResponse.success(CommonSuccessCode.OK);
        } else {
            System.out.println("Controller out");
            return ServnowResponse.fail(UserErrorCode.CERTIFICATION_NUMBER_MISMATCH);
        }
    }

    @GetMapping("/users/me/survey/{id}")
    public ServnowResponse<MySurveysResultResponse> getMySurveysResult(@Parameter (hidden = true) @UserId final Long userId, @PathVariable(name = "id") long surveyId) {
        MySurveysResultResponse result = resultQueryService.getMySurveysResult(surveyId);
        return ServnowResponse.success(CommonSuccessCode.OK, result);
    }

    @PostMapping("/users/me/survey/{id}/memo")
    public ServnowResponse<Object> saveInsightMemo(@Parameter (hidden = true) @UserId final Long userId, @PathVariable(name = "id") long surveyId, @RequestBody final MySurveysResultMemoRequest request) {
        resultCommandService.saveInsightMemo(surveyId, request);
        return ServnowResponse.success(CommonSuccessCode.OK);
    }

    @GetMapping("/users/me/survey/{id}/memo/list")
    public ServnowResponse<MySurveysResultMemoResponse> getInsightMemo(@Parameter (hidden = true) @UserId final Long userId, @PathVariable(name = "id") long surveyId) {
        MySurveysResultMemoResponse result = resultQueryService.getInsightMemo(surveyId);
        return ServnowResponse.success(CommonSuccessCode.OK, result);
    }

    @GetMapping("/users/me/survey") // sort=newest, sort=oldest, sort=participants
    public ServnowResponse<List<MySurveyResponse>> getMySurveys(@Parameter (hidden = true) @UserId final Long userId, @RequestParam(value = "sort", required = false, defaultValue = "newest") String sort) {
        List<MySurveyResponse> surveys = surveyQueryService.getMySurveys(userId, sort);
        return ServnowResponse.success(CommonSuccessCode.OK, surveys);
    }

    @GetMapping("/users/me/point")
    public ServnowResponse<UserPointGetResponse> getUserPoint(@Parameter(hidden = true) @UserId final Long userId) {
        return ServnowResponse.success(CommonSuccessCode.OK, userQueryService.getUserPoint(userId));
    }

    @DeleteMapping("/users/me/survey/{id}/memo")
    public ServnowResponse<Void> deleteSurveyMemo(@PathVariable(name = "id") Long id) {
        resultCommandService.deleteSurveyMemo(id);
        return ServnowResponse.success(CommonSuccessCode.OK);
    }
}
