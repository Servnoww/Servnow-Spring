package servnow.servnow.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

  INVALID_PLATFORM_TYPE(HttpStatus.NOT_FOUND, "등록되지 않은 플랫폼 타입입니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
