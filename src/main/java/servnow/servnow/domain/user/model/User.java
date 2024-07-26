package servnow.servnow.domain.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import servnow.servnow.domain.common.BaseTimeEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String platform;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Integer point;

    @Column(nullable = false)
    private Integer answerCount;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private Boolean status;

    private LocalDateTime deletedAt;
}
