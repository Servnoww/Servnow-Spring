package servnow.servnow.api.question.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.domain.question.model.MultipleChoice;
import servnow.servnow.domain.question.repository.MultipleChoiceRepository;

@Component
@RequiredArgsConstructor
public class MultipleChoiceUpdater {

  private final MultipleChoiceRepository multipleChoiceRepository;

  public void save(final MultipleChoice multipleChoice) {
    multipleChoiceRepository.save(multipleChoice);
  }
}