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
}