package servnow.servnow.domain.surveyresultmemo.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import servnow.servnow.domain.question.model.Question;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SurveyResultMemo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_result_memo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false)
    private String title;

    @Column(name = "question_order", nullable = false)
    private Integer questionOrder;

    @Column(length = 300)
    private String content;

    private SurveyResultMemo(Question question, String title, Integer questionOrder, String content) {
        this.question = question;
        this.title = title;
        this.questionOrder = questionOrder;
        this.content = content;
    }

    public static SurveyResultMemo create(Question question, String content) {
        return new SurveyResultMemo(question, question.getTitle(), question.getQuestionOrder(), content);
    }

    public void updateContent(String content) {
        this.content = content;
    }
}



