package servnow.servnow.api.result.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import servnow.servnow.api.result.dto.request.MySurveysResultMemoRequest;
import servnow.servnow.api.result.dto.response.MySurveysResultResponse;
import servnow.servnow.common.code.SurveyErrorCode;
import servnow.servnow.common.exception.NotFoundException;
import servnow.servnow.domain.question.model.enums.QuestionType;
import servnow.servnow.domain.surveyresult.repository.SurveyResultRepository;
import servnow.servnow.domain.survey.model.Survey;
import servnow.servnow.domain.question.model.Question;
import servnow.servnow.domain.surveyresult.model.SurveyResult;
import servnow.servnow.domain.subjectiveresult.model.SubjectiveResult;
import servnow.servnow.domain.question.model.MultipleChoice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultQueryService {

    private final SurveyResultRepository surveyResultRepository;

    @Transactional(readOnly = true)
    public MySurveysResultResponse getMySurveysResult(long surveyId) {
        Survey survey = surveyResultRepository.findSurveyWithSectionsById(surveyId);
        if (survey == null) {
            throw new NotFoundException(SurveyErrorCode.SURVEY_NOT_FOUND);
        }

        List<Question> questions = surveyResultRepository.findQuestionsBySurveyId(surveyId);
        List<SurveyResult> surveyResults = surveyResultRepository.findSurveyResultsBySurveyId(surveyId);
        List<SubjectiveResult> subjectiveResults = surveyResultRepository.findSubjectiveResultsBySurveyId(surveyId);

        // MultipleChoiceResult를 통해 응답 수 집계
        Map<Long, Long> choiceResultsMap = surveyResults.stream()
                .flatMap(surveyResult -> surveyResult.getMultipleChoiceResults().stream())
                .collect(Collectors.groupingBy(result -> result.getMultipleChoice().getId(), Collectors.counting()));

        Map<Long, List<SubjectiveResult>> subjectiveResultMap = subjectiveResults.stream()
                .collect(Collectors.groupingBy(sr -> sr.getQuestion().getId()));

        List<MySurveysResultResponse.SectionResult> sectionResults = survey.getSections().stream()
                .map(section -> {
                    List<MySurveysResultResponse.QuestionResult> questionResults = questions.stream()
                            .filter(question -> question.getSection().equals(section))
                            .map(question -> {
                                MySurveysResultResponse.QuestionResult.QuestionType questionType =
                                        mapQuestionType(question.getQuestionType());

                                List<MySurveysResultResponse.QuestionResult.ChoiceResult> choices =
                                        mapChoices(question.getMultipleChoices(), choiceResultsMap);

                                List<MySurveysResultResponse.QuestionResult.SubjectiveResponse> responses =
                                        mapSubjectiveResponses(subjectiveResultMap.getOrDefault(question.getId(), new ArrayList<>()));

                                return new MySurveysResultResponse.QuestionResult(
                                        question.getQuestionOrder(),
                                        question.getTitle(),
                                        questionType,
                                        question.isEssential(),
                                        question.isDuplicate(),
                                        question.isHasNextSection(),
                                        choices,
                                        questionType == MySurveysResultResponse.QuestionResult.QuestionType.SUBJECTIVE_SHORT ? responses : List.of()
                                );
                            })
                            .collect(Collectors.toList());

                    return new MySurveysResultResponse.SectionResult(
                            section.getTitle(),
                            section.getContent(),
                            section.getNextSectionNo(),
                            questionResults
                    );
                })
                .collect(Collectors.toList());

        return new MySurveysResultResponse(
                survey.getId(),
                surveyResults.size(),
                sectionResults
        );
    }

    private MySurveysResultResponse.QuestionResult.QuestionType mapQuestionType(QuestionType type) {
        return switch (type) {
            case MULTIPLE_CHOICE -> MySurveysResultResponse.QuestionResult.QuestionType.MULTIPLE_CHOICE;
            case SUBJECTIVE_LONG -> MySurveysResultResponse.QuestionResult.QuestionType.SUBJECTIVE_LONG;
            case SUBJECTIVE_SHORT -> MySurveysResultResponse.QuestionResult.QuestionType.SUBJECTIVE_SHORT;
            default -> throw new IllegalArgumentException("Unknown question type: " + type);
        };
    }

    private List<MySurveysResultResponse.QuestionResult.ChoiceResult> mapChoices(List<MultipleChoice> choices, Map<Long, Long> choiceResultsMap) {
        return choices.stream()
                .map(choice -> new MySurveysResultResponse.QuestionResult.ChoiceResult(
                        choice.getId(),
                        choice.getContent(),
                        choiceResultsMap.getOrDefault(choice.getId(), 0L).intValue() // 응답 수 계산
                ))
                .collect(Collectors.toList());
    }

    private List<MySurveysResultResponse.QuestionResult.SubjectiveResponse> mapSubjectiveResponses(List<SubjectiveResult> results) {
        return results.stream()
                .map(result -> new MySurveysResultResponse.QuestionResult.SubjectiveResponse(
                        result.getSurveyResult().getId(),
                        result.getContent()
                ))
                .collect(Collectors.toList());
    }

}
