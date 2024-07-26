package servnow.servnow.domain.answer.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import servnow.servnow.domain.common.BaseTimeEntity;

@Entity
@Getter
@Table(name = "subjective")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subjective extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subjectiveId;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false, length = 500)
    private String content;

    private Long nextSectionId;
}
