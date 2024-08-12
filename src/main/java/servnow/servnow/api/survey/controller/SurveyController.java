package servnow.servnow.api.survey.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.api.survey.dto.request.SurveyPostRequest;
import servnow.servnow.api.survey.dto.response.HomeSurveyGetResponse;
import servnow.servnow.api.survey.dto.response.SurveyGetResponse;
import servnow.servnow.api.survey.dto.response.SurveyIntroGetResponse;
import servnow.servnow.api.survey.dto.response.SurveySearchGetResponse;
import servnow.servnow.api.survey.service.SurveyCommandService;
import servnow.servnow.api.survey.service.SurveyQueryService;
import servnow.servnow.auth.UserId;
import servnow.servnow.common.code.CommonSuccessCode;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SurveyController {

  private final SurveyCommandService surveyCommandService;
  private final SurveyQueryService surveyQueryService;


  @PostMapping("/survey")
  public ServnowResponse<Void> createSurvey(@UserId Long userId, @RequestBody @Valid SurveyPostRequest surveyPostRequest) {
    // 유저 임시생성, 추후 아이디 로직 머지 후 고칠 예정
    surveyCommandService.createSurvey(userId, surveyPostRequest);
    return ServnowResponse.success(CommonSuccessCode.CREATED);
  }

  @GetMapping("/survey/{id}/intro")
  public ServnowResponse<SurveyIntroGetResponse> getSurveyIntro(@PathVariable(name = "id") long id) {
    return ServnowResponse.success(CommonSuccessCode.OK, surveyQueryService.getSurveyIntro(id));
  }

  @GetMapping("/survey/guest/{id}/intro")
  public ServnowResponse<SurveyIntroGetResponse> getSurveyIntroForGuest(@PathVariable(name = "id") long id) {
    return ServnowResponse.success(CommonSuccessCode.OK, surveyQueryService.getSurveyIntro(id));
  }

  @GetMapping("/survey/{id}/sections/{sectionOrder}")
  public ServnowResponse<SurveyGetResponse> getSurveySection(@PathVariable(name = "id") long surveyId, @PathVariable(name = "sectionOrder") int sectionOrder) {
    return ServnowResponse.success(CommonSuccessCode.OK, surveyQueryService.getSurveySection(surveyId, sectionOrder));
  }

  @GetMapping("/survey/guest/{id}/sections/{sectionOrder}")
  public ServnowResponse<SurveyGetResponse> getSurveySectionForGuest(@PathVariable(name = "id") long surveyId, @PathVariable(name = "sectionOrder") int sectionOrder) {
    return ServnowResponse.success(CommonSuccessCode.OK, surveyQueryService.getSurveySection(surveyId, sectionOrder));
  }

  @GetMapping("/survey")
  public ServnowResponse<List<SurveySearchGetResponse>> searchSurvey(@RequestParam(name = "keyword") String keyword, @RequestParam(name = "filter", defaultValue = "false") boolean filter) {
    return ServnowResponse.success(CommonSuccessCode.OK, surveyQueryService.searchSurvey(1L, keyword, filter));
  }
  
  @GetMapping("/survey/home")
  public ServnowResponse<List<HomeSurveyGetResponse>> getHome(@RequestParam(name = "sort", defaultValue = "deadline") String sort) {
    // 유저 임시 생성, 추후 아이디 로직 머지 후 고칠 예정
    return ServnowResponse.success(CommonSuccessCode.OK, surveyQueryService.getSurveyList(1L, sort));
  }
}