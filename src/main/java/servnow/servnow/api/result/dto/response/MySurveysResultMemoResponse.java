package servnow.servnow.api.result.dto.response;


import java.util.List;
import java.util.Map;

public record MySurveysResultMemoResponse(
        List<QuestionMemo> questions
) {
    public record QuestionMemo(
            Long questionId,
            int questionOrder,
            String title,
//            List<String> memos,
            List<Map<Long, String>> memos
    ) {}
}

