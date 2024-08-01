package servnow.servnow.api.survey.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SurveyPostRequest(
  String title,
  String content1,
  String content2,
  int duration,
  String characterType,
  String mainColor,
  String subColor,
  String font,
  String reward,
  int rewardCount,
  LocalDateTime expiredAt,
  List<SurveyPostSection> surveyPostSection
) {
    public record SurveyPostSection(
        String sectionTitle,
        String sectionContent,
        int sectionOrder,
        Integer nextSectionNo,
        List<SurveyPostQuestion> surveyPostQuestions
    ) {}

    public record SurveyPostQuestion(
        String questionTitle,
        String questionContent,
        int questionOrder,
        String questionType,
        boolean isEssential,
        boolean isDuplicate,
        boolean hasNextSection,
        List<SurveyPostAnswer> surveyPostAnswers
    ) {}

    public record SurveyPostAnswer(
        String answerContent,
        int nextSectionNo
    ) {
    }
}
