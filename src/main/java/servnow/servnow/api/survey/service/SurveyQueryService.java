package servnow.servnow.api.survey.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import servnow.servnow.api.survey.dto.response.SurveyIntroGetResponse;
import servnow.servnow.domain.survey.model.Survey;

@Service
@RequiredArgsConstructor
public class SurveyQueryService {

  private final SurveyFinder surveyFinder;

  @Transactional(readOnly = true)
  public SurveyIntroGetResponse getSurveyIntro(final Long userId, final long id) {
    Survey survey = surveyFinder.findByIdWithSectionsAndQuestions(id);
    return SurveyIntroGetResponse.of(survey, (userId != null), countQuestion(survey));
  }

  private int countQuestion(final Survey survey) {
    return survey.getSections().stream()
        .mapToInt(section -> section.getQuestions().size())
        .sum();
  }
}