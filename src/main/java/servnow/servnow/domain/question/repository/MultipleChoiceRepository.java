package servnow.servnow.domain.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servnow.servnow.domain.question.model.MultipleChoice;

public interface MultipleChoiceRepository extends JpaRepository<MultipleChoice, Long> {
}