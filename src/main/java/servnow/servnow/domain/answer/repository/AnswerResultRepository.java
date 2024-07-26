package servnow.servnow.domain.answer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servnow.servnow.domain.answer.model.AnswerResult;
import servnow.servnow.domain.question.model.Question;

public interface AnswerResultRepository extends JpaRepository<AnswerResult, Long> {

}
