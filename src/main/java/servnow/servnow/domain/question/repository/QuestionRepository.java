package servnow.servnow.domain.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servnow.servnow.domain.question.model.Question;
import servnow.servnow.domain.user.model.User;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}