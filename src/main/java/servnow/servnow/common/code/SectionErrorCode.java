package servnow.servnow.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SectionErrorCode implements ErrorCode {

  SECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 섹션이 존재하지 않습니다.");

  private final HttpStatus httpStatus;
  private final String message;
}