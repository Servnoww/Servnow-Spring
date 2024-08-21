package servnow.servnow.api.survey.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.api.survey.dto.response.HomeSurveyGetResponse;
import servnow.servnow.api.user.dto.response.MySurveyResponse;
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

  public List<HomeSurveyGetResponse> findByKeyword(final long userId, final String keyword, final boolean filter) {
    List<Survey> surveyList = filter
      ? surveyRepository.findAllByKeywordWithFilter(keyword)
      : surveyRepository.findAllByKeyword(keyword);

    return surveyList.stream()
            .map(survey -> HomeSurveyGetResponse.of(survey, userId))
            .collect(Collectors.toList());
  }

  public List<MySurveyResponse> findAllSurveys(final long userId, String sort) {

    List<Survey> surveys = switch (sort) {
        case "newest" -> surveyRepository.findByUserIdOrderByCreatedAtDesc(userId);
        case "oldest" -> surveyRepository.findByUserIdOrderByCreatedAtAsc(userId);
        case "participants" -> surveyRepository.findAllOrderByParticipantCountDesc(userId);
        default -> throw new IllegalArgumentException("Invalid sort option: " + sort);
    };

// sort=newest, sort=oldest, sort=participants
      // Survey 객체를 MySurveyResponse로 변환하여 반환
    return surveys.stream()
            .map(MySurveyResponse::of)
            .collect(Collectors.toList());


  }
  
  public List<HomeSurveyGetResponse> findAllOrderBy(final long userId, final String sort) {
    List<Survey> surveyList = switch (sort) {
      case "deadline" -> surveyRepository.findAllOrderByExpiredAtDesc();
      case "participants" -> surveyRepository.findAllOrderByParticipantsDesc();
      default -> throw new BadRequestException(SurveyErrorCode.SURVEY_INVALID_SORT);
    };

    return surveyList.stream()
            .map(survey -> HomeSurveyGetResponse.of(survey, userId))
            .collect(Collectors.toList());
  }
}
