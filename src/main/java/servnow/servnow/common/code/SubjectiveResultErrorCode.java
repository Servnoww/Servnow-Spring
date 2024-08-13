package servnow.servnow.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SubjectiveResultErrorCode implements ErrorCode {

    SUBJECTIVE_RESULT_MULTIPLE_CHOICE_ID_PRESENT(HttpStatus.BAD_REQUEST, "주관식 답변에는 객관식id가 없어야 합니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
