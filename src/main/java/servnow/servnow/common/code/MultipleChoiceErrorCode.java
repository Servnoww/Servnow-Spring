package servnow.servnow.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MultipleChoiceErrorCode implements ErrorCode {

    MULTIPLE_CHOICE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 객관식이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
