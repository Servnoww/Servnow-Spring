package servnow.servnow.api.result.dto.response;

import java.util.List;

public record MySurveysResultResponse(
        Long surveyId,
        int response,
        List<SectionResult> sections
) {
    public record SectionResult(
            String sectionTitle,
            String sectionContent,
            int nextSection,
            List<QuestionResult> questions
    ) {}

    public record QuestionResult(
            int questionNumber,
            String questionTitle,
            String questionContent,
            QuestionType questionType,
            boolean isRequired,
            boolean allowMultipleResponses,
            boolean includeInField,
            List<ChoiceResult> choices,
            List<SubjectiveResponse> responses
    ) {
        public enum QuestionType {
            MULTIPLE_CHOICE,
            SUBJECTIVE_LONG,
            SUBJECTIVE_SHORT
        }

        public record ChoiceResult(
                Long multipleChoiceId,
                String multipleChoiceContent,
                int response
        ) {}

        public record SubjectiveResponse(
                Long respondentId,
                String responseContent
        ) {}
    }
}
