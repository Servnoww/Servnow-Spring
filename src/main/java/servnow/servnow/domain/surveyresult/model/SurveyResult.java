package servnow.servnow.domain.surveyresult.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import servnow.servnow.domain.common.BaseTimeEntity;
import servnow.servnow.domain.multiplechoiceresult.model.MultipleChoiceResult;
import servnow.servnow.domain.subjectiveresult.model.SubjectiveResult;
import servnow.servnow.domain.survey.model.Survey;
import servnow.servnow.domain.user.model.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SurveyResult extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_result_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @Email
    private String email;

    @OneToMany(mappedBy = "surveyResult")
    private List<MultipleChoiceResult> multipleChoiceResults = new ArrayList<>();

    @OneToMany(mappedBy = "surveyResult")
    private List<SubjectiveResult> subjectiveResults = new ArrayList<>();
}