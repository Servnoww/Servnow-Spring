package servnow.servnow.api.survey.dto.response;

import java.util.List;
import servnow.servnow.domain.question.model.MultipleChoice;
import servnow.servnow.domain.question.model.Question;
import servnow.servnow.domain.question.model.enums.QuestionType;
import servnow.servnow.domain.section.model.Section;

public record SurveyGetResponse(
    long surveyId,
    String sectionTitle,
    String sectionContent,
    int sectionOrder,
    int sectionTotalCount,
    int nextSectionNo,
    List<SurveyGetQuestion> questions
) {

  public static SurveyGetResponse of(final Section section, final int sectionTotalCount) {
    return new SurveyGetResponse(
        section.getSurvey().getId(),
        section.getTitle(),
        section.getContent(),
        section.getSectionOrder(),
        sectionTotalCount,
        section.getNextSectionNo(),
        section.getQuestions().stream().map(SurveyGetQuestion::of).toList()
        );
  }

  public record SurveyGetQuestion(
      long questionId,
      String questionTitle,
      String questionContent,
      int questionOrder,
      QuestionType questionType,
      boolean isEssential,
      boolean isDuplicate,
      boolean hasNextSection,
      List<SurveyGetAnswer> answers
  ){
    public static SurveyGetQuestion of(final Question question) {
      return new SurveyGetQuestion(
          question.getId(),
          question.getTitle(),
          question.getContent(),
          question.getQuestionOrder(),
          question.getQuestionType(),
          question.isEssential(),
          question.isDuplicate(),
          question.isHasNextSection(),
          question.getMultipleChoices().stream().map(SurveyGetAnswer::of).toList());
    }

    public record SurveyGetAnswer(
      String answerContent,
      Integer nextSectionNo
    ) {
      public static SurveyGetAnswer of(final MultipleChoice multipleChoice) {
        return new SurveyGetAnswer(
            multipleChoice.getContent(),
            multipleChoice.getNextSectionNo()
        );
      }
    }
  }
}