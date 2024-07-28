package servnow.servnow.domain.user.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {

  ACTIVE("ACTIVE"),
  INACTIVE("INACTIVE");

  private final String value;
}