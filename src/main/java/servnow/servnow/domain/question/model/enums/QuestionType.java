package servnow.servnow.domain.question.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionType {

  MULTIPLE_CHOICE("MULTIPLE_CHOICE"),
  SUBJECTIVE_LONG("SUBJECTIVE_LONG"),
  SUBJECTIVE_SHORT("SUBJECTIVE_SHORT");

  private final String value;
}
