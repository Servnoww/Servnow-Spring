package servnow.servnow.domain.survey.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import servnow.servnow.domain.common.BaseTimeEntity;
import servnow.servnow.domain.section.model.Section;
import servnow.servnow.domain.survey.model.enums.CharacterType;
import servnow.servnow.domain.surveyresult.model.SurveyResult;
import servnow.servnow.domain.user.model.User;
import servnow.servnow.domain.user.model.enums.Platform;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Survey extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content1;

    @Column(nullable = false)
    private String content2;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private String mainColor;

    @Column(nullable = false)
    private String subColor;

    @Column(nullable = false)
    private String font;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CharacterType characterType;

    private String reward;

    private Integer rewardCount;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @OneToMany(mappedBy = "survey")
    private List<Section> sections = new ArrayList<>();

    @OneToMany(mappedBy = "survey")
    private List<SurveyResult> surveyResults = new ArrayList<>();

    public static Survey create(User user, String title, String content1, String content2, int duration, String mainColor, String subColor, String font, CharacterType characterType, String reward, Integer rewardCount, LocalDateTime expiredAt) {
        return Survey.builder()
            .user(user)
            .title(title)
            .content1(content1)
            .content2(content2)
            .duration(duration)
            .mainColor(mainColor)
            .subColor(subColor)
            .font(font)
            .characterType(characterType)
            .reward(reward)
            .rewardCount(rewardCount)
            .expiredAt(expiredAt)
            .build();
    }
}