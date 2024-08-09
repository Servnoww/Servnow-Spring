package servnow.servnow.api.user.controller.response;

import servnow.servnow.domain.survey.model.Survey;
import servnow.servnow.domain.survey.model.enums.CharacterType;
import servnow.servnow.domain.surveyresult.model.SurveyResult;

import java.time.LocalDateTime;
import java.util.List;

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
