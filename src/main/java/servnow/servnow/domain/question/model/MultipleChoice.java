package servnow.servnow.domain.question.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import servnow.servnow.domain.common.BaseTimeEntity;

@Entity
@Getter
@Table(name = "multiple_choice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MultipleChoice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long multipleChoiceId;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false, length = 50)
    private String content;

    private Integer nextSectionNo;
}
