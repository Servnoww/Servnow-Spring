package servnow.servnow.domain.user.model.enums;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import servnow.servnow.common.code.AuthErrorCode;
import servnow.servnow.common.exception.BadRequestException;

@Getter
@RequiredArgsConstructor
public enum Platform {

  KAKAO("KAKAO"),
  SERVNOW("SERVNOW");

  private final String loginPlatform;

  public static Platform getEnumPlatformFromStringPlatform(String loginPlatform) {
    return Arrays.stream(values())
        .filter(platform -> platform.loginPlatform.equals(loginPlatform))
        .findFirst()
        .orElseThrow(() -> new BadRequestException(AuthErrorCode.INVALID_PLATFORM_TYPE));
  }
}