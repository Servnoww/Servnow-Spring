package servnow.servnow.domain.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import servnow.servnow.domain.common.BaseTimeEntity;
import servnow.servnow.domain.user.model.enums.Gender;
import servnow.servnow.domain.user.model.enums.Level;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_info_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String nickname;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate birth;

    @Lob
    private String profile_url;

    private String refreshToken;

    @Column(nullable = false)
    private int point;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Level level;
}