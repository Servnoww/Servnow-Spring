package servnow.servnow.api.result.service.surveyresultmemo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.domain.surveyresultmemo.repository.SurveyResultMemoRepository;

@Component
@RequiredArgsConstructor
public class SurveyResultMemoUpdater {

  private final SurveyResultMemoRepository surveyResultMemoRepository;

  public void deleteById(final long id) {
    surveyResultMemoRepository.deleteById(id);
  }
}
