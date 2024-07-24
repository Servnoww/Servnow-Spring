package servnow.servnow.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import servnow.servnow.common.code.ErrorCode;
import servnow.servnow.common.code.SuccessCode;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServnowResponse<T> {

  private final int code;
  private final String message;
  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private T data;

  public static <T> ServnowResponse<T> success(SuccessCode success) {
    return new ServnowResponse<>(success.getHttpStatus().value(), success.getMessage());
  }

  public static <T> ServnowResponse<T> success(SuccessCode success, T data) {
    return new ServnowResponse<>(success.getHttpStatus().value(), success.getMessage(), data);
  }

  public static <T> ServnowResponse<T> fail(ErrorCode error) {
    return new ServnowResponse<>(error.getHttpStatus().value(), error.getMessage());
  }
}
