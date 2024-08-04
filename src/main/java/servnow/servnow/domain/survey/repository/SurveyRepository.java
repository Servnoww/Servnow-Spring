package servnow.servnow.domain.survey.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import servnow.servnow.domain.survey.model.Survey;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

  @Query("select s from Survey s join fetch s.sections sec where s.id = :id")
  Optional<Survey> findByIdWithSections(@Param("id") long id);
}