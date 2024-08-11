package servnow.servnow.api.survey.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.api.survey.dto.response.SurveySearchGetResponse;
import servnow.servnow.common.code.SurveyErrorCode;
import servnow.servnow.common.exception.BadRequestException;
import servnow.servnow.common.exception.NotFoundException;
import servnow.servnow.domain.survey.model.Survey;
import servnow.servnow.domain.survey.repository.SurveyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SurveyFinder {

  private final SurveyRepository surveyRepository;

  public Survey findByIdWithSectionsAndQuestions(final long id) {
    return surveyRepository.findByIdWithSections(id).orElseThrow(() -> new NotFoundException(SurveyErrorCode.SURVEY_NOT_FOUND));
  }

  public List<SurveySearchGetResponse> findByKeyword(final long userId, final String keyword, final boolean filter) {
    List<Survey> surveyList = filter
      ? surveyRepository.findAllByKeywordWithFilter(keyword)
      : surveyRepository.findAllByKeyword(keyword);

    return surveyList.stream()
            .map(survey -> SurveySearchGetResponse.of(survey, userId))
            .collect(Collectors.toList());
  }
}
