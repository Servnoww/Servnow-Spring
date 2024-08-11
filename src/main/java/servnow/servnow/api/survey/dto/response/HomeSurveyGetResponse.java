package servnow.servnow.api.survey.dto.response;

import servnow.servnow.domain.survey.model.Survey;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public record HomeSurveyGetResponse(
        long surveyId,
        long sectionNum,
        String title,
        LocalDateTime createdAt,
        LocalDateTime expiredAt,
        int dDay,
        int participants,
        boolean status
) {
    public static HomeSurveyGetResponse of(final Survey survey, final long userId) {
        int dDay = (int) ChronoUnit.DAYS.between(LocalDateTime.now(), survey.getExpiredAt());
        int participants = survey.getSurveyResults().size();
        boolean status = survey.getSurveyResults().stream().anyMatch(result -> result.getUser().getId() == userId);

        return new HomeSurveyGetResponse(
                survey.getId(),
                1,
                survey.getTitle(),
                survey.getCreatedAt(),
                survey.getExpiredAt(),
                dDay,
                participants,
                status
        );
    }
}
