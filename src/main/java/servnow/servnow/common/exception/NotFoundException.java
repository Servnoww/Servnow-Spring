package servnow.servnow.common.exception;

import lombok.Getter;
import servnow.servnow.common.code.ErrorCode;

@Getter
public class NotFoundException extends RuntimeException {

  private final ErrorCode errorCode;

  public NotFoundException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
