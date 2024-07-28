package servnow.servnow.domain.survey.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CharacterType {

  TYPE_ONE("TYPE_ONE"),
  TYPE_TWO("TYPE_TWO"),
  TYPE_THREE("TYPE_THREE"),
  TYPE_FOUR("TYPE_FOUR"),
  TYPE_FIVE("TYPE_FIVE"),
  TYPE_SIX("TYPE_SIX"),
  TYPE_SEVEN("TYPE_SEVEN"),
  TYPE_EIGHT("TYPE_EIGHT"),
  TYPE_NINE("TYPE_NINE"),
  TYPE_TEN("TYPE_TEN"),
  TYPE_ELEVEN("TYPE_ELEVEN"),
  TYPE_TWELVE("TYPE_TWELVE");

  private final String value;
}