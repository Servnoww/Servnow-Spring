package servnow.servnow.domain.surveyresult.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import servnow.servnow.api.result.dto.response.UserSurveyAnswerResultResponse;
import servnow.servnow.domain.survey.model.Survey;
import servnow.servnow.domain.question.model.Question;
import servnow.servnow.domain.surveyresult.model.SurveyResult;
import servnow.servnow.domain.subjectiveresult.model.SubjectiveResult;

import java.util.List;

public interface SurveyResultRepository extends JpaRepository<SurveyResult, Long> {

    boolean existsByUserIdAndSurveyId(Long userId, Long surveyId);

    @Query("SELECT s FROM Survey s LEFT JOIN FETCH s.sections WHERE s.id = :surveyId")
    Survey findSurveyWithSectionsById(@Param("surveyId") Long surveyId);

    @Query("SELECT q FROM Question q WHERE q.section.survey.id = :surveyId")
    List<Question> findQuestionsBySurveyId(@Param("surveyId") Long surveyId);

    @Query("SELECT sr FROM SurveyResult sr WHERE sr.survey.id = :surveyId")
    List<SurveyResult> findSurveyResultsBySurveyId(@Param("surveyId") Long surveyId);

    @Query("SELECT sbr FROM SubjectiveResult sbr WHERE sbr.surveyResult.survey.id = :surveyId")
    List<SubjectiveResult> findSubjectiveResultsBySurveyId(@Param("surveyId") Long surveyId);

    List<SurveyResult> findByUserId(long userId);

    // SurveyResult에 대해 사용자 ID로 필터링된 결과를 반환하는 쿼리
    @Query("SELECT sr FROM SurveyResult sr WHERE sr.survey.id = :surveyId AND sr.user.id = :userId")
    List<SurveyResult> findSurveyResultsBySurveyIdAndUserId(@Param("surveyId") long surveyId, @Param("userId") Long userId);

    // SubjectiveResult에 대해 사용자 ID로 필터링된 결과를 반환하는 쿼리
    @Query("SELECT sbr FROM SubjectiveResult sbr WHERE sbr.surveyResult.survey.id = :surveyId AND sbr.surveyResult.user.id = :userId")
    List<SubjectiveResult> findSubjectiveResultsBySurveyIdAndUserId(@Param("surveyId") Long surveyId, @Param("userId") Long userId);
}
