package servnow.servnow.domain.answer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import servnow.servnow.domain.common.BaseTimeEntity;
import servnow.servnow.domain.survey.model.Survey;
import servnow.servnow.domain.user.model.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "answer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseTimeEntity {
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

