package servnow.servnow.api.user.dto.response;

import servnow.servnow.domain.user.model.UserInfo;

public record UserPointGetResponse(
    int point
) {
  public static UserPointGetResponse of(final UserInfo userInfo) {
    return new UserPointGetResponse(userInfo.getPoint());
  }
}