package servnow.servnow.api.survey.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.api.user.dto.response.MySurveyResponse;
import servnow.servnow.common.code.SurveyErrorCode;
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


}
