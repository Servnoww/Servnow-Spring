package servnow.servnow.domain.answer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servnow.servnow.domain.question.model.Question;

public interface AnswerRepository extends JpaRepository<Question, Long> {

}
