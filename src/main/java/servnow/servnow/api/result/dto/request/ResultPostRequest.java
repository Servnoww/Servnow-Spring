package servnow.servnow.api.result.dto.request;


import lombok.Getter;
import servnow.servnow.domain.multiplechoiceresult.model.MultipleChoiceResult;
import servnow.servnow.domain.question.model.MultipleChoice;
import servnow.servnow.domain.question.model.Question;
import servnow.servnow.domain.subjectiveresult.model.SubjectiveResult;
import servnow.servnow.domain.survey.model.Survey;
import servnow.servnow.domain.surveyresult.model.SurveyResult;
import servnow.servnow.domain.user.model.User;

import java.util.List;

public record ResultPostRequest(
        String email,
        List<Answer> answers // Answer 객체 리스트
) {
    public SurveyResult toEntity(final User user, final Survey survey) {
        return SurveyResult.create(user, survey, email);
    }

    public record Answer(
            Long questionId,
            Long multipleChoiceId, // 객관식 선택 시 사용
            String content // 주관식 답변 시 사용
    ) {
        public MultipleChoiceResult toMultipleChoiceEntity(final SurveyResult surveyResult, MultipleChoice multipleChoice, Question question) {
            return MultipleChoiceResult.create(question, surveyResult, multipleChoice);
        }

        public SubjectiveResult toSubjectiveEntity(final SurveyResult surveyResult, Question question, String content) {
            return SubjectiveResult.create(question, surveyResult, content);
        }
    }
}

