package servnow.servnow.api.question.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.domain.question.model.Question;
import servnow.servnow.domain.question.repository.QuestionRepository;

@Component
@RequiredArgsConstructor
public class QuestionUpdater {

  private final QuestionRepository questionRepository;

  public void saveAll(final List<Question> questions) {
    questionRepository.saveAll(questions);
  }
}
