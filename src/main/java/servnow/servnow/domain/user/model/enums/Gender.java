package servnow.servnow.domain.user.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {

  MALE("MALE"),
  FEMALE("FEMALE");

  private final String value;
}
