package servnow.servnow.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserInfoErrorCode implements ErrorCode {

  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."),
  USER_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저 정보가 존재하지 않습니다.");

  private final HttpStatus httpStatus;
  private final String message;
}