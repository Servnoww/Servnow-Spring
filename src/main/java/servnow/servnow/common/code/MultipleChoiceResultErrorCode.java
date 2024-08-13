package servnow.servnow.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MultipleChoiceResultErrorCode implements ErrorCode {

    MULTIPLE_CHOICE_CONTENT_PRESENT(HttpStatus.BAD_REQUEST, "객관식 답변에는 content가 없어야 합니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
