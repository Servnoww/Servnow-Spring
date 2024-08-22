package servnow.servnow.api.survey.service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import servnow.servnow.api.survey.dto.response.HomeSurveyGetResponse;
import servnow.servnow.api.user.dto.response.MySurveyResponse;
import servnow.servnow.common.code.SurveyErrorCode;
import servnow.servnow.common.exception.BadRequestException;
import servnow.servnow.common.exception.NotFoundException;
import servnow.servnow.domain.survey.model.Survey;
import servnow.servnow.domain.survey.repository.SurveyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SurveyFinder {

  private final SurveyRepository surveyRepository;

  public Survey findByIdWithSectionsAndQuestions(final long id) {
    return surveyRepository.findByIdWithSections(id).orElseThrow(() -> new NotFoundException(SurveyErrorCode.SURVEY_NOT_FOUND));
  }

  public List<HomeSurveyGetResponse> findByKeyword(final long userId, final List<String> keywords, final boolean filter) {
    Specification<Survey> spec = (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      for (String keyword : keywords) {
        Predicate titlePredicate = cb.like(root.get("title"), "%" + keyword + "%");
        predicates.add(titlePredicate);
      }

      Predicate keywordPredicate = cb.or(predicates.toArray(new Predicate[0]));
      Predicate expiredAtPredicate = cb.greaterThanOrEqualTo(root.get("expiredAt"), cb.currentDate());

      if (filter) {
        Predicate rewardPredicate = cb.isNotNull(root.get("reward"));
        return cb.and(keywordPredicate, expiredAtPredicate, rewardPredicate);
      } else {
        return cb.and(keywordPredicate, expiredAtPredicate);
      }
    };

    List<Survey> surveyList = surveyRepository.findAll(spec);

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
