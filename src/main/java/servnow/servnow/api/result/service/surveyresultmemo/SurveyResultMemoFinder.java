package servnow.servnow.api.result.service.surveyresultmemo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.common.code.SurveyResultMemoErrorCode;
import servnow.servnow.common.exception.NotFoundException;
import servnow.servnow.domain.surveyresultmemo.model.SurveyResultMemo;
import servnow.servnow.domain.surveyresultmemo.repository.SurveyResultMemoRepository;

@Component
@RequiredArgsConstructor
public class SurveyResultMemoFinder {

  private final SurveyResultMemoRepository surveyResultMemoRepository;

  public SurveyResultMemo findById(final long id) {
      return surveyResultMemoRepository.findById(id).orElseThrow(
          () -> new NotFoundException(SurveyResultMemoErrorCode.SURVEY_RESULT_MEMO_NOT_FOUND));
  }
}
