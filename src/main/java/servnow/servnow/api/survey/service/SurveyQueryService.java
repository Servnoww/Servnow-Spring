package servnow.servnow.api.survey.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import servnow.servnow.api.section.service.SectionFinder;
import servnow.servnow.api.survey.dto.response.HomeSurveyGetResponse;
import servnow.servnow.api.user.dto.response.MySurveyResponse;
import servnow.servnow.api.survey.dto.response.SurveyGetResponse;
import servnow.servnow.api.survey.dto.response.SurveyIntroGetResponse;
import servnow.servnow.api.survey.dto.response.SurveySearchGetResponse;
import servnow.servnow.domain.survey.model.Survey;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyQueryService {

  private final SurveyFinder surveyFinder;
  private final SectionFinder sectionFinder;

  @Transactional(readOnly = true)
  public SurveyIntroGetResponse getSurveyIntro(final long id) {
    Survey survey = surveyFinder.findByIdWithSectionsAndQuestions(id);
    return SurveyIntroGetResponse.of(survey, countQuestion(survey));
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

  @Transactional(readOnly = true)
  public List<SurveySearchGetResponse> searchSurvey(final Long userId, final String keyword, final boolean filter) {
    return surveyFinder.findByKeyword(userId, keyword, filter);
  }
  
  @Transactional(readOnly = true)
  public List<HomeSurveyGetResponse> getSurveyList(final Long userId, final String sort) {
    return surveyFinder.findAllOrderBy(userId, sort);
  }

  @Transactional(readOnly = true)
    public List<MySurveyResponse> getMySurveys(final Long userId, String sort) {
      return surveyFinder.findAllSurveys(userId, sort);
    }
}