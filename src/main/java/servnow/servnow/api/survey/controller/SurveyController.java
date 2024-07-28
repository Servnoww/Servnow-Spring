package servnow.servnow.api.survey.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import servnow.servnow.api.survey.service.SurveyCommandService;
import servnow.servnow.api.survey.service.SurveyQueryService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SurveyController {
  private final SurveyCommandService surveyCommandService;
  private final SurveyQueryService surveyQueryService;
}