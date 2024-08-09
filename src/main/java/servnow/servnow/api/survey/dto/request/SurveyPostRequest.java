package servnow.servnow.api.survey.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import servnow.servnow.domain.question.model.MultipleChoice;
import servnow.servnow.domain.question.model.Question;
import servnow.servnow.domain.question.model.enums.QuestionType;
import servnow.servnow.domain.section.model.Section;
import servnow.servnow.domain.survey.model.Survey;
import servnow.servnow.domain.survey.model.enums.CharacterType;
import servnow.servnow.domain.user.model.User;

public record SurveyPostRequest(
    @NotBlank(message = "제목이 비었거나 null입니다.")
    String title,
    @NotBlank(message = "소개글이 비었거나 null입니다.")
    String content1,
    @NotBlank(message = "소개글이 비었거나 null입니다.")
    String content2,
    int duration,
    @NotNull(message = "캐릭터 타입이 null입니다.")
    CharacterType characterType,
    @NotNull(message = "메인 컬러가 null입니다.")
    String mainColor,
    @NotNull(message = "서브 컬러가 null입니다.")
    String subColor,
    @NotNull(message = "폰트가 null입니다.")
    String font,
    String reward,
    Integer rewardCount,
    @NotNull(message = "만료 기한이 null입니다.")
    String expiredAt,
    @Size(min = 1, message = "섹션은 최소 1개 이상이어야 합니다.")
    List<SurveyPostSection> sections
) {

  public Survey toEntity(final User user) {
    return Survey.create(user, title, content1, content2, duration, mainColor, subColor, font,
        characterType, reward, rewardCount, LocalDateTime.parse(expiredAt));
  }

  public record SurveyPostSection(
      @NotBlank(message = "섹션 제목이 비었거나 null입니다.")
      String sectionTitle,
      @NotBlank(message = "섹션 내용이 비었거나 null입니다.")
      String sectionContent,
      int sectionOrder,
      int nextSectionNo,
      @Size(min = 1, message = "섹션에 포함된 질문은 최소 1개 이상이어야 합니다.")
      List<SurveyPostQuestion> questions
  ) {

    public Section toEntity(final Survey survey) {
      return Section.create(survey, sectionOrder, sectionTitle, sectionContent, nextSectionNo);
    }

    public record SurveyPostQuestion(
        @NotBlank(message = "질문 제목이 비었거나 null입니다.")
        String questionTitle,
        String questionContent,
        int questionOrder,
        @NotNull(message = "질문 타입이 null입니다.")
        QuestionType questionType,
        boolean isEssential,
        boolean isDuplicate,
        boolean hasNextSection,
        List<SurveyPostAnswer> answers
    ) {

      public Question toEntity(final Section section) {
        return Question.create(section, questionOrder, questionTitle, questionContent, questionType, isEssential,
            isDuplicate, hasNextSection);
      }

      public record SurveyPostAnswer(
          @NotNull(message = "객관식의 내용이 null입니다.")
          String answerContent,
          Integer nextSectionNo
      ) {

        public MultipleChoice toEntity(final Question question) {
          return MultipleChoice.create(question, answerContent, nextSectionNo);
        }
      }
    }
  }
}