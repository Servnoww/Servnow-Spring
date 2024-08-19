package servnow.servnow.api.result.dto.request;


import java.util.List;

public record MySurveysResultMemoRequest(
        List<QuestionMemo> questions
) {
    public record QuestionMemo(
            Long questionId,
            String title,
            int questionOrder,
            List<String> memos
    ) {}
}
