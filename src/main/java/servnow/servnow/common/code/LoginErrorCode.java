package servnow.servnow.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LoginErrorCode implements ErrorCode {
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
  AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다."),
  ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생하였습니다."),
  ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 가입된 계정입니다."),
  ALREADY_EXISTED_ID(HttpStatus.CONFLICT, "중복된 아이디입니다."),
  USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
  PASSWORDS_DO_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
  INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "이메일 형식이 적합하지 않습니다."),
  USERNAME_TOO_SHORT(HttpStatus.BAD_REQUEST, "아이디가 너무 짧습니다."),
  INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST, "비밀번호는 8~20자, 영문, 숫자, 특수문자 혼합으로 입력해주세요."),
  INVALID_BIRTHDATE(HttpStatus.BAD_REQUEST, "유효하지 않은 생년월일입니다."),
  MISSING_REQUIRED_FIELD(HttpStatus.BAD_REQUEST, "필수 항목이 누락되었습니다."),
  INVALID_PW_KAKAO_USER(HttpStatus.BAD_REQUEST, "카카오 계정은 비밀번호를 변경할 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
