package servnow.servnow.api.survey.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.api.survey.dto.request.SurveyPostRequest;
import servnow.servnow.api.survey.dto.response.*;
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
  public ServnowResponse<Void> createSurvey(@Parameter(hidden = true) @UserId Long userId, @RequestBody @Valid SurveyPostRequest surveyPostRequest) {
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
  public ServnowResponse<HomeSurveyListGetResponse> searchSurvey(@Parameter(hidden = true) @UserId Long userId, @RequestParam(name = "keyword") String keyword, @RequestParam(name = "filter", defaultValue = "false") boolean filter) {
    // 로그인 하지 않은 경우
    if (userId == null) {
      return ServnowResponse.success(CommonSuccessCode.OK, surveyQueryService.searchSurvey(0L, keyword, filter));
    }
    return ServnowResponse.success(CommonSuccessCode.OK, surveyQueryService.searchSurvey(userId, keyword, filter));
  }
  
  @GetMapping("/survey/home")
  public ServnowResponse<HomeSurveyListGetResponse> getHome(@Parameter(hidden = true) @UserId Long userId, @RequestParam(name = "sort", defaultValue = "deadline") String sort) {
    // 로그인 하지 않은 경우
    if (userId == null) {
      return ServnowResponse.success(CommonSuccessCode.OK, surveyQueryService.getSurveyList(0L, sort));
    }
    return ServnowResponse.success(CommonSuccessCode.OK, surveyQueryService.getSurveyList(userId, sort));
  }

}