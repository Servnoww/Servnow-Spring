package servnow.servnow.domain.user.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import servnow.servnow.common.code.AuthErrorCode;
import servnow.servnow.common.code.LoginErrorCode;
import servnow.servnow.common.exception.BadRequestException;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Gender {

  MALE("MALE"),
  FEMALE("FEMALE");

  private final String value;

  public static Gender getEnumGenderFromStringGender(String loginGender) {
    return Arrays.stream(values())
        .filter(gender -> gender.value.equals(loginGender.toUpperCase()))
        .findFirst()
        .orElseThrow(() -> new BadRequestException(LoginErrorCode.INVALID_GENDER_TYPE));
  }
}
