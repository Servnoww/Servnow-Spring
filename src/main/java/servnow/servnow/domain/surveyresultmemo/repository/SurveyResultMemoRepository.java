package servnow.servnow.domain.surveyresultmemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import servnow.servnow.domain.surveyresultmemo.model.SurveyResultMemo;

import java.util.List;

public interface SurveyResultMemoRepository extends JpaRepository<SurveyResultMemo, Long> {

    Long countByQuestionId(Long questionId);

}
