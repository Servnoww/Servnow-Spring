package servnow.servnow.domain.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;

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
    private String birth;

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
            final String nickname,
            final Gender gender,
            final String email,
            final String birthDate,
            final String refreshToken,
            final String profile_url
            ) {
        return UserInfo.builder()
                .user(user)
                .refreshToken(refreshToken)
                .nickname(nickname)
                .birth(birthDate)
                .email(email)
                .profile_url(profile_url)
                .gender(gender)
                .point(0)
                .level(Level.COMMONER)
                .build();
  }
  
  public void updatePoint(int increment) {
        this.point += increment;
  }
  public void incrementPoint(int increment) {
      this.point += increment;
  }
  public void setProfile_url(String profileUrl) {
      this.profile_url = profileUrl;
  }
  public void setEmail(String email) {
      this.email = email;
  }

  public void updateRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}