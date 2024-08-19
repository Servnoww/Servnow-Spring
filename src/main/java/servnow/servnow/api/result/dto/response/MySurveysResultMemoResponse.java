package servnow.servnow.api.result.dto.response;


import java.util.List;

public record MySurveysResultMemoResponse(
        List<QuestionMemo> questions
) {
    public record QuestionMemo(
            Long questionId,
            int questionOrder,
            String title,
            List<String> memos
    ) {}
}

