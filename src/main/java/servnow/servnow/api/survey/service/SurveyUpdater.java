package servnow.servnow.api.survey.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.domain.survey.model.Survey;
import servnow.servnow.domain.survey.repository.SurveyRepository;

@Component
@RequiredArgsConstructor
public class SurveyUpdater {

  private final SurveyRepository surveyRepository;

  public Survey save(final Survey survey) {
    return surveyRepository.save(survey);
  }
}
