package servnow.servnow.domain.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servnow.servnow.domain.question.model.Question;
import servnow.servnow.domain.question.model.Subjective;

public interface SubjectiveRepository extends JpaRepository<Subjective, Long> {

}
