package servnow.servnow.api.user.dto.response;

import servnow.servnow.domain.survey.model.Survey;
import servnow.servnow.domain.survey.model.enums.CharacterType;

import java.time.LocalDateTime;

public record MySurveyResponse(
        long surveyId,
        CharacterType characterType,
        String title,
        LocalDateTime createdAt,
        int entry

) {
    public static MySurveyResponse of(final Survey survey) {
        return new MySurveyResponse(
                survey.getId(),
                survey.getCharacterType(),
                survey.getTitle(),
                survey.getCreatedAt(),
                survey.getSurveyResults().size()
        );
    }
}
