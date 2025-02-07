package servnow.servnow.domain.section.model;

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
import servnow.servnow.domain.question.model.Question;
import servnow.servnow.domain.survey.model.Survey;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Section extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @Column(nullable = false)
    private int sectionOrder;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int nextSectionNo;

    @OneToMany(mappedBy = "section")
    @BatchSize(size = 100)
    private List<Question> questions = new ArrayList<>();

    public static Section create(Survey survey, int sectionOrder, String title, String content, int nextSectionNo) {
        return Section.builder().survey(survey).sectionOrder(sectionOrder).title(title).content(content).nextSectionNo(nextSectionNo).build();
    }
}