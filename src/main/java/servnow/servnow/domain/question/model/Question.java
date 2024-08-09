package servnow.servnow.domain.question.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import servnow.servnow.domain.common.BaseTimeEntity;
import servnow.servnow.domain.multiplechoiceresult.model.MultipleChoiceResult;
import servnow.servnow.domain.question.model.enums.QuestionType;
import servnow.servnow.domain.section.model.Section;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(nullable = false)
    private int questionOrder;

    @Column(nullable = false)
    private String title;

    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Column(nullable = false)
    private boolean isEssential;

    @Column(nullable = false)
    private boolean isDuplicate;

    @Column(nullable = false)
    private boolean hasNextSection;

    @OneToMany(mappedBy = "question")
    @BatchSize(size = 100)
    private List<MultipleChoice> multipleChoices = new ArrayList<>();

    @OneToMany(mappedBy = "question")
    private List<MultipleChoiceResult> multipleChoiceResults = new ArrayList<>();

    public static Question create(Section section, int questionOrder, String title, String content, QuestionType questionType, boolean isEssential, boolean isDuplicate, boolean hasNextSection){
        return Question.builder().section(section).questionOrder(questionOrder).title(title).content(content).questionType(questionType).isEssential(isEssential).isDuplicate(isDuplicate).hasNextSection(hasNextSection).build();
    }
}