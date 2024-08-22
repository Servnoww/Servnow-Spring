package servnow.servnow.api.user.dto.response;

import servnow.servnow.domain.user.model.UserInfo;
import servnow.servnow.domain.user.model.enums.Platform;

public record KakaoEditProfilePageResponse(
        String profile_url,
        String nickname,
        Platform platform
) implements EditProfilePageResponse{
    public static KakaoEditProfilePageResponse of (UserInfo userInfo) {
        return new KakaoEditProfilePageResponse(userInfo.getProfile_url(), userInfo.getNickname(), userInfo.getUser().getPlatform());
                // 카카오는 이름, 번호가 필요한데 해당 창이 없음
    }
}

