package servnow.servnow.api.result.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.domain.surveyresult.model.SurveyResult;
import servnow.servnow.domain.surveyresult.repository.SurveyResultRepository;

@Component
@RequiredArgsConstructor
public class ResultUpdater {

    private final SurveyResultRepository surveyResultRepository;

    public SurveyResult save(final SurveyResult surveyResult) {return surveyResultRepository.save(surveyResult);}
}
