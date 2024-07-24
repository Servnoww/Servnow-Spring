package servnow.servnow.common.exception;

import lombok.Getter;
import servnow.servnow.common.code.ErrorCode;

@Getter
public class UnauthorizedException extends RuntimeException {
  private final ErrorCode errorCode;

  public UnauthorizedException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
