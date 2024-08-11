package servnow.servnow.common.code;

public enum RegisterErrorCode {
    USER_ID_REQUIRED("E001", "아이디는 필수 입력 사항입니다."),
    USER_ID_INVALID("E002", "아이디는 영문, 숫자, 특수문자 포함 8~20자 입니다."),
    PASSWORD_INVALID("E003", "비밀번호는 8~20자, 영문, 숫자, 특수문자 혼합으로 입력해주세요."),
    PASSWORD_MISMATCH("E004", "비밀번호가 일치하지 않습니다."),
    EMAIL_REQUIRED("E005", "이메일을 입력해 주세요."),
    EMAIL_INVALID("E006", "이메일 형식이 적합하지 않습니다."),
    VERIFICATION_CODE_MISMATCH("E007", "인증번호가 일치하지 않습니다.");

    private final String code;
    private final String message;

    RegisterErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
