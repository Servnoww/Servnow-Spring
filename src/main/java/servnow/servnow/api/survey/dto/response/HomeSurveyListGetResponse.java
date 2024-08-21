package servnow.servnow.api.survey.dto.response;

import java.util.List;

public record HomeSurveyListGetResponse(
        List<HomeSurveyGetResponse> survey
) {
    public static HomeSurveyListGetResponse of(List<HomeSurveyGetResponse> survey) {
        return new HomeSurveyListGetResponse(survey);
    }
}
