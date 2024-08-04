package servnow.servnow.domain.section.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import servnow.servnow.domain.section.model.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {

  @Query("select s from Section s join fetch s.questions where s.survey.id = :surveyId and s.sectionOrder = :sectionOrder ")
  Optional<Section> findBySurveyIdAndSectionOrderWithQuestions(@Param("surveyId") long surveyId, @Param("sectionOrder") int sectionOrder);

  long countBySurveyId(long surveyId);
}