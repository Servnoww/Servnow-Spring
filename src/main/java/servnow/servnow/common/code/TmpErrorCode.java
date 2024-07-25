package servnow.servnow.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TmpErrorCode implements ErrorCode {

  TMP_NOT_FOUND(HttpStatus.NOT_FOUND, "등록되지 않은 tmp입니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
