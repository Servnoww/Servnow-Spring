package servnow.servnow.domain.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import java.util.Date;

import lombok.*;
import servnow.servnow.domain.common.BaseTimeEntity;
import servnow.servnow.domain.user.model.enums.Gender;
import servnow.servnow.domain.user.model.enums.Level;

@Entity
@Getter
@Setter
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

    public static UserInfo createMemberInfo(
            final User user,
            final String refreshToken,
            final String nickname,
            final String gender,
            final String email,
            final LocalDate birthDate,
            final String url
            ) {
        return UserInfo.builder()
                .user(user)
                .refreshToken(refreshToken)
                .profile_url(url)
                .nickname(nickname)
                .birth(birthDate)
                .email(email)
                .gender(Gender.valueOf(gender))
                .point(0)
                .level(Level.COMMONER)
                .build();
    }
}