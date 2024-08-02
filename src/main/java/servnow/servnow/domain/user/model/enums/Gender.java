package servnow.servnow.domain.user.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {

  MALE("male"),
  FEMALE("female");

  private final String value;
}
