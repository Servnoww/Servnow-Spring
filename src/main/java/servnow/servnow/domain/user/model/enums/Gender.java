package servnow.servnow.domain.user.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import servnow.servnow.common.code.AuthErrorCode;
import servnow.servnow.common.exception.BadRequestException;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Gender {

  MALE("male"),
  FEMALE("female");

  private final String value;

  public static Gender getEnumGenderFromStringGender(String loginGender) {
    return Arrays.stream(values())
        .filter(gender -> gender.value.equals(loginGender))
        .findFirst()
        .orElseThrow(() -> new BadRequestException(AuthErrorCode.valueOf("ERROR")));
  }
}
