package servnow.servnow.domain.question.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import servnow.servnow.domain.multiplechoiceresult.model.MultipleChoiceResult;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MultipleChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "multiple_choice_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false)
    private String content;

    private Integer nextSectionNo;

    @OneToMany(mappedBy = "multipleChoice")
    private List<MultipleChoiceResult> multipleChoiceResults = new ArrayList<>();


    public static MultipleChoice create(Question question, String content, Integer nextSectionNo) {
        return MultipleChoice.builder().question(question).content(content).nextSectionNo(nextSectionNo).build();
    }
}