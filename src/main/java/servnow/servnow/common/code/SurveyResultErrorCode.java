package servnow.servnow.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SurveyResultErrorCode implements ErrorCode {

    SURVEY_ALREADY_SUBMITTED(HttpStatus.BAD_REQUEST, "이미 제출된 설문입니다."),
    ERROR_MEMO_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "각 질문당 메모는 최대 4개까지 가능합니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
