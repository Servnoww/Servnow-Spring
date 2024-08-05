package servnow.servnow.api.user.dto.response;

import servnow.servnow.domain.user.model.User;
import servnow.servnow.domain.user.model.UserInfo;

public record ServnowEditProfilePageResponse(
        String profile_url,
        String nickname,
        String serialId,
        String email
) implements EditProfilePageResponse{
    public static ServnowEditProfilePageResponse of (UserInfo userInfo, User user) {
        return new ServnowEditProfilePageResponse(userInfo.getProfile_url(), userInfo.getNickname(), user.getSerialId(), userInfo.getEmail());
    }
}
