package servnow.servnow.api.survey.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.api.survey.dto.response.HomeSurveyGetResponse;
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
