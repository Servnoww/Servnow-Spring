package servnow.servnow.domain.survey.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servnow.servnow.domain.survey.model.Survey;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

}
