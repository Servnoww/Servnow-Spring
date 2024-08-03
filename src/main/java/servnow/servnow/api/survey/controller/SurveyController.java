package servnow.servnow.api.survey.controller;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.api.survey.dto.request.SurveyPostRequest;
import servnow.servnow.api.survey.dto.response.SurveyIntroGetResponse;
import servnow.servnow.api.survey.service.SurveyCommandService;
import servnow.servnow.api.survey.service.SurveyQueryService;
import servnow.servnow.common.code.CommonSuccessCode;

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
    return ServnowResponse.success(CommonSuccessCode.OK, surveyQueryService.getSurveyIntro(null, id));
  }
}