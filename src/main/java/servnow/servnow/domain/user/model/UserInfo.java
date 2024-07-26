package servnow.servnow.domain.user.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import servnow.servnow.domain.common.BaseTimeEntity;

@Entity
@Table(name = "user_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userInfoId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String refreshToken;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private Boolean gender;

    @Column(nullable = false)
    private String birth;

    @Lob
    private String profile;
}

