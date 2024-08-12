package servnow.servnow.domain.survey.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import servnow.servnow.domain.survey.model.Survey;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

  @Query("select s from Survey s join fetch s.sections sec where s.id = :id")
  Optional<Survey> findByIdWithSections(@Param("id") long id);

  @Query("select s from Survey s left join fetch s.surveyResults sr where s.title like %:keyword%")
  List<Survey> findAllByKeyword(@Param("keyword") String keyword);

  @Query("select s from Survey s left join fetch s.surveyResults sr where s.title like %:keyword% and s.reward is not null")
  List<Survey> findAllByKeywordWithFilter(@Param("keyword") String keyword);

  // userId로 Survey 목록 조회, createdAt 기준 오름차순 정렬
  List<Survey> findByUserIdOrderByCreatedAtAsc(long userId);

  // userId로 Survey 목록 조회, createdAt 기준 내림차순 정렬
  List<Survey> findByUserIdOrderByCreatedAtDesc(long userId);

  // 설문지 참여자 수에 따라 정렬
  @Query("SELECT s FROM Survey s LEFT JOIN s.surveyResults sr WHERE s.user.id = :userId GROUP BY s.id ORDER BY COUNT(sr.id) DESC")
  List<Survey> findAllOrderByParticipantCountDesc(@Param("userId") long userId);

  @Query("select s from Survey s left join fetch s.surveyResults order by s.expiredAt desc")
  List<Survey> findAllOrderByExpiredAtDesc();

  @Query("select s from Survey s left join fetch s.surveyResults order by size(s.surveyResults) desc")
  List<Survey> findAllOrderByParticipantsDesc();
}