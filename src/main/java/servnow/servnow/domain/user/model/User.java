package servnow.servnow.domain.user.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import servnow.servnow.domain.common.BaseTimeEntity;

import java.time.LocalDateTime;
import servnow.servnow.domain.survey.model.Survey;
import servnow.servnow.domain.surveyresult.model.SurveyResult;
import servnow.servnow.domain.user.model.enums.Platform;
import servnow.servnow.domain.user.model.enums.UserRole;
import servnow.servnow.domain.user.model.enums.UserStatus;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Platform platform;

    // 일반 로그인의 id, 카카오의 경우 serial_id 저장
    @Column(nullable = false)
    private String serialId;

    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "user")
    private List<Survey> surveys = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<SurveyResult> surveyResults = new ArrayList<>();
}