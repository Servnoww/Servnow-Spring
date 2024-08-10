package servnow.servnow.api.survey.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.api.survey.dto.request.SurveyPostRequest;
import servnow.servnow.api.survey.dto.response.HomeSurveyGetResponse;
import servnow.servnow.api.survey.dto.response.SurveyGetResponse;
import servnow.servnow.api.survey.dto.response.SurveyIntroGetResponse;
import servnow.servnow.api.survey.service.SurveyCommandService;
import servnow.servnow.api.survey.service.SurveyQueryService;
import servnow.servnow.common.code.CommonSuccessCode;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SurveyController {

  private final SurveyCommandService surveyCommandService;
  private final SurveyQueryService surveyQueryService;


  @PostMapping("/survey")
  public ServnowResponse<Void> createSurvey(@RequestBody @Valid SurveyPostRequest surveyPostRequest) {
    // 유저 임시생성, 추후 아이디 로직 머지 후 고칠 예정
    surveyCommandService.createSurvey(1L, surveyPostRequest);
    return ServnowResponse.success(CommonSuccessCode.CREATED);
  }

  @GetMapping("/survey/{id}/intro")
  public ServnowResponse<SurveyIntroGetResponse> getSurveyIntro(@PathVariable(name = "id") long id) {
    // 유저 임시생성, 추후 아이디 로직 머지 후 고칠 예정
    return ServnowResponse.success(CommonSuccessCode.OK, surveyQueryService.getSurveyIntro(1L, id));
  }

  @GetMapping("/survey/{id}/sections/{sectionOrder}")
  public ServnowResponse<SurveyGetResponse> getSurveySection(@PathVariable(name = "id") long surveyId, @PathVariable(name = "sectionOrder") int sectionOrder) {
    return ServnowResponse.success(CommonSuccessCode.OK, surveyQueryService.getSurveySection(surveyId, sectionOrder));
  }

  @GetMapping("/survey/home")
  public ServnowResponse<List<HomeSurveyGetResponse>> getHome(@RequestParam(name = "sort", defaultValue = "deadline") String sort) {
    // 유저 임시 생성, 추후 아이디 로직 머지 후 고칠 예정
    return ServnowResponse.success(CommonSuccessCode.OK, surveyQueryService.getSurveyList(1L, sort));
  }
}