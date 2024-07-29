package servnow.servnow.domain.multiplechoiceresult.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servnow.servnow.domain.multiplechoiceresult.model.MultipleChoiceResult;

public interface MultipleChoicerResultRepository extends JpaRepository<MultipleChoiceResult, Long> {
}