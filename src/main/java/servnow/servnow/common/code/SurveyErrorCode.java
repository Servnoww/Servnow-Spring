package servnow.servnow.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SurveyErrorCode implements ErrorCode {

  SURVEY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 설문이 존재하지 않습니다."),
  SURVEY_INVALID_SORT(HttpStatus.BAD_REQUEST, "정렬 기준이 잘못되었습니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
