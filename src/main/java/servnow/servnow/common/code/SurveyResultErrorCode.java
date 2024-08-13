package servnow.servnow.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SurveyResultErrorCode implements ErrorCode {

    SURVEY_ALREADY_SUBMITTED(HttpStatus.BAD_REQUEST, "이미 제출된 설문입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
