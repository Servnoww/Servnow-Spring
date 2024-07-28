package servnow.servnow.domain.question.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import servnow.servnow.domain.common.BaseTimeEntity;
import servnow.servnow.domain.multiplechoiceresult.model.MultipleChoiceResult;
import servnow.servnow.domain.question.model.enums.QuestionType;
import servnow.servnow.domain.section.model.Section;
import servnow.servnow.domain.subjectiveresult.model.SubjectiveResult;

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
    private List<MultipleChoice> multipleChoices = new ArrayList<>();

    @OneToOne(mappedBy = "question")
    private SubjectiveResult subjectiveResults;

    @OneToMany(mappedBy = "question")
    private List<MultipleChoiceResult> multipleChoiceResults = new ArrayList<>();
}