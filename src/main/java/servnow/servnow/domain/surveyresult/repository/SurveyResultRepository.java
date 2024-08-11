package servnow.servnow.domain.surveyresult.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import servnow.servnow.domain.survey.model.Survey;
import servnow.servnow.domain.section.model.Section;
import servnow.servnow.domain.question.model.Question;
import servnow.servnow.domain.surveyresult.model.SurveyResult;
import servnow.servnow.domain.subjectiveresult.model.SubjectiveResult;

import java.util.List;

public interface SurveyResultRepository extends JpaRepository<SurveyResult, Long> {

    @Query("SELECT s FROM Survey s LEFT JOIN FETCH s.sections WHERE s.id = :surveyId")
    Survey findSurveyWithSectionsById(@Param("surveyId") Long surveyId);

    @Query("SELECT q FROM Question q WHERE q.section.survey.id = :surveyId")
    List<Question> findQuestionsBySurveyId(@Param("surveyId") Long surveyId);

    @Query("SELECT sr FROM SurveyResult sr WHERE sr.survey.id = :surveyId")
    List<SurveyResult> findSurveyResultsBySurveyId(@Param("surveyId") Long surveyId);

    @Query("SELECT sbr FROM SubjectiveResult sbr WHERE sbr.surveyResult.survey.id = :surveyId")
    List<SubjectiveResult> findSubjectiveResultsBySurveyId(@Param("surveyId") Long surveyId);
}
