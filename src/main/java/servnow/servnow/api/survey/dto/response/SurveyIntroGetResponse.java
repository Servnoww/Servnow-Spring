package servnow.servnow.api.survey.dto.response;

import java.time.LocalDateTime;
import servnow.servnow.domain.survey.model.Survey;
import servnow.servnow.domain.survey.model.enums.CharacterType;

public record SurveyIntroGetResponse(
    int duration,
    int questionCount,
    CharacterType characterType,
    String reward,
    Integer rewardCount,
    String title,
    String content1,
    String content2,
    LocalDateTime createdAt,
    LocalDateTime expiredAt
) {
  public static SurveyIntroGetResponse of(final Survey survey, final int questionCount) {
    return new SurveyIntroGetResponse(
        Math.round((float) survey.getDuration() / 60),
        questionCount,
        survey.getCharacterType(),
        survey.getReward(),
        survey.getRewardCount(),
        survey.getTitle(),
        survey.getContent1(),
        survey.getContent2(),
        survey.getCreatedAt(),
        survey.getExpiredAt());
  }
}
