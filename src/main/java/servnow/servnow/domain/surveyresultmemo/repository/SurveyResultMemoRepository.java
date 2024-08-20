package servnow.servnow.domain.surveyresultmemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servnow.servnow.domain.surveyresultmemo.model.SurveyResultMemo;


public interface SurveyResultMemoRepository extends JpaRepository<SurveyResultMemo, Long> {

    Long countByQuestionId(Long questionId);

}
