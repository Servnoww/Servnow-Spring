package servnow.servnow.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."),
    PLATFORM_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 플랫폼은 존재하지 않습니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호 입력 칸을 비워주세요."),
    DUPLICATE_SERIAL_ID(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디 입니다."),
    // Mail 관련
    EMAIL_DUPLICATE(HttpStatus.BAD_REQUEST, "해당 이메일은 이미 사용 중입니다."),
    SEND_CERTIFICATION_NUMBER(HttpStatus.BAD_REQUEST, "인증번호 전송을 실패하였습니다."),
    CERTIFICATION_NUMBER_MISMATCH(HttpStatus.UNAUTHORIZED, "인증번호가 일치하지 않습니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "인증이 진행되지 않았습니다."),

            ;


    private final HttpStatus httpStatus;
    private final String message;
}
