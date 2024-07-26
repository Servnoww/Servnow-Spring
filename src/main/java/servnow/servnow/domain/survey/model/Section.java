package servnow.servnow.domain.survey.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import servnow.servnow.domain.common.BaseTimeEntity;

@Entity
@Getter
@Table(name = "section")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sectionId;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @Column(nullable = false)
    private Integer order;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private String nextSectionNo;
}
