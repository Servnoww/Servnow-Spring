package servnow.servnow.api.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import servnow.servnow.api.dto.ServnowResponse;
import org.springframework.web.bind.annotation.*;
import servnow.servnow.api.result.dto.response.MySurveysResultResponse;
import servnow.servnow.api.result.service.ResultQueryService;
import servnow.servnow.api.user.controller.response.MyPageResponse;
import servnow.servnow.api.user.service.UserCommandService;
import servnow.servnow.api.user.service.UserQueryService;
import servnow.servnow.common.code.CommonSuccessCode;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final ResultQueryService resultQueryService;


    // 아직 유저 정보를 넘기는 방식이 정해지지 않아서 UserQueryService에서 userId값을 고정하여 테스트 함
    @GetMapping("/users/me")
    public ServnowResponse<MyPageResponse> getMyPage() {
        return ServnowResponse.success(CommonSuccessCode.OK, userQueryService.getMyPage());
    }

    @GetMapping("/users/me/survey/{id}")
    public ServnowResponse<MySurveysResultResponse> getMySurveysResult(@PathVariable(name = "id") long surveyId) {
        MySurveysResultResponse result = resultQueryService.getMySurveysResult(surveyId);
        return ServnowResponse.success(CommonSuccessCode.OK, result);
    }}

