package servnow.servnow.api.survey.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import servnow.servnow.api.section.service.SectionFinder;
import servnow.servnow.api.survey.dto.response.SurveyGetResponse;
import servnow.servnow.api.survey.dto.response.SurveyIntroGetResponse;
import servnow.servnow.domain.survey.model.Survey;

@Service
@RequiredArgsConstructor
public class SurveyQueryService {

  private final SurveyFinder surveyFinder;
  private final SectionFinder sectionFinder;

  @Transactional(readOnly = true)
  public SurveyIntroGetResponse getSurveyIntro(final Long userId, final long id) {
    Survey survey = surveyFinder.findByIdWithSectionsAndQuestions(id);
    return SurveyIntroGetResponse.of(survey, (userId != null), countQuestion(survey));
  }

  @Transactional(readOnly = true)
  public SurveyGetResponse getSurveySection(final long surveyId, final int sectionOrder) {
    return SurveyGetResponse.of(sectionFinder.findBySurveyIdAndSectionOrderWithQuestions(surveyId, sectionOrder), (int)sectionFinder.countBySurvey(surveyId));
  }

  private int countQuestion(final Survey survey) {
    return survey.getSections().stream()
        .mapToInt(section -> section.getQuestions().size())
        .sum();
  }
}