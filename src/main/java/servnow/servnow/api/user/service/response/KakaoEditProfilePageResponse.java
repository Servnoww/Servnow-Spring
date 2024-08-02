package servnow.servnow.api.user.service.response;

import servnow.servnow.domain.user.model.UserInfo;

public record KakaoEditProfilePageResponse(
        String profile_url,
        String nickname
//        String name,
//        String phone
) implements EditProfilePageResponse{
    public static KakaoEditProfilePageResponse of (UserInfo userInfo) {
        return new KakaoEditProfilePageResponse(userInfo.getNickname(), userInfo.getProfile_url());
                // 카카오는 이름, 번호가 필요한데 해당 창이 없음
    }
}

