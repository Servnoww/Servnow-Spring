package servnow.servnow.domain.surveyResult.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servnow.servnow.domain.question.model.Question;
import servnow.servnow.domain.surveyResult.model.SurveyResult;

public interface SurveyResultRepository extends JpaRepository<SurveyResult, Long> {

}
