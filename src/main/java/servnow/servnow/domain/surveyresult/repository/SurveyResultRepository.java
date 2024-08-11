package servnow.servnow.domain.surveyresult.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servnow.servnow.domain.surveyresult.model.SurveyResult;

public interface SurveyResultRepository extends JpaRepository<SurveyResult, Long> {
    boolean existsByUserIdAndSurveyId(Long userId, Long surveyId);
}