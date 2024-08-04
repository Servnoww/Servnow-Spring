package servnow.servnow.api.survey.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.common.code.SurveyErrorCode;
import servnow.servnow.common.exception.NotFoundException;
import servnow.servnow.domain.survey.model.Survey;
import servnow.servnow.domain.survey.repository.SurveyRepository;

@Component
@RequiredArgsConstructor
public class SurveyFinder {

  private final SurveyRepository surveyRepository;

  public Survey findByIdWithSectionsAndQuestions(final long id) {
    return surveyRepository.findByIdWithSections(id).orElseThrow(() -> new NotFoundException(SurveyErrorCode.SURVEY_NOT_FOUND));
  }
}
