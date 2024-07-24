package servnow.servnow.common.exception;

import servnow.servnow.common.code.ErrorCode;

public class InternalServerException extends RuntimeException {
  private final ErrorCode errorCode;

  public InternalServerException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}