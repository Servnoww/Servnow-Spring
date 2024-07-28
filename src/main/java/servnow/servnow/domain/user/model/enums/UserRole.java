package servnow.servnow.domain.user.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

  ADMIN("ADMIN"),
  USER("USER");

  private final String value;

}