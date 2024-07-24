package servnow.servnow.api.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.common.code.BusinessErrorCode;
import servnow.servnow.common.exception.BadRequestException;
import servnow.servnow.common.exception.NotFoundException;
import servnow.servnow.common.exception.UnauthorizedException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  public ServnowResponse<Void> handleBadRequestException(BadRequestException e) {
    log.error("handleBadRequestException() in GlobalExceptionHandler throw BadRequestException : {}", e.getMessage());
    return ServnowResponse.fail(e.getErrorCode());
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ServnowResponse<Void> handleUnauthorizedException(UnauthorizedException e) {
    log.error("handleUnauthorizedException() in GlobalExceptionHandler throw UnauthorizedException : {}", e.getMessage());
    return ServnowResponse.fail(e.getErrorCode());
  }

  @ExceptionHandler(NotFoundException.class)
  public ServnowResponse<Void> handleEntityNotFoundException(NotFoundException e) {
    log.error("handleEntityNotFoundException() in GlobalExceptionHandler throw EntityNotFoundException : {}", e.getMessage());
    return ServnowResponse.fail(e.getErrorCode());
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ServnowResponse<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
    log.error("handleMissingServletRequestParameterException() in GlobalExceptionHandler throw MissingServletRequestParameterException : {}", e.getMessage());
    return ServnowResponse.fail(BusinessErrorCode.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ServnowResponse<Void> handleException(Exception e) {
    log.error("handleException() in GlobalExceptionHandler throw Exception [{}] : {}", e.getClass() , e.getMessage());
    return ServnowResponse.fail(BusinessErrorCode.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ServnowResponse<Void> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    log.error("handleMethodArgumentNotValidException() in GlobalExceptionHandler throw MethodArgumentNotValidException : {}", e.getMessage());
    return ServnowResponse.fail(BusinessErrorCode.BAD_REQUEST);
  }
}
