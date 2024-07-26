package servnow.servnow.domain.surveyResult.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import servnow.servnow.domain.common.BaseTimeEntity;
import servnow.servnow.domain.survey.model.Survey;
import servnow.servnow.domain.user.model.User;

@Entity
@Getter
@Table(name = "survey_result")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyResult extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerResultId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @Email
    private String email;
}

