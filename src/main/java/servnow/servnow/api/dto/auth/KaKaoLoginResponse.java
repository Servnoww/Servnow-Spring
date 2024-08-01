package servnow.servnow.api.dto.auth;

import lombok.*;

@Builder
@Data@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KaKaoLoginResponse {

    private Long id;
    private String connected_at;
    private KakaoProperties properties;
    private KakaoAccount kakao_account;

    @Getter
    public class KakaoProperties {
        private String nickname;
        private String profile_image;
        private String thumbnail_image;
    }
    @Getter
    public class KakaoAccount {
        private Boolean profile_nickname_needs_agreement;
        private Boolean profile_image_needs_agreement;
        private KakaoProfile profile;
    }
    @Getter
    public class KakaoProfile {
        private String nickname;
        private String thumbnail_image_url;
        private String profile_image_url;
        private Boolean is_default_image;
    }
}
