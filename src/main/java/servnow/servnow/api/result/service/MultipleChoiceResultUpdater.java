package servnow.servnow.api.result.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.domain.multiplechoiceresult.model.MultipleChoiceResult;
import servnow.servnow.domain.multiplechoiceresult.repository.MultipleChoicerResultRepository;

@Component
@RequiredArgsConstructor
public class MultipleChoiceResultUpdater {
    private final MultipleChoicerResultRepository multipleChoiceResultRepository;

    public void save(final MultipleChoiceResult multipleChoiceResult) {multipleChoiceResultRepository.save(multipleChoiceResult);}


}
