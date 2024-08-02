package servnow.servnow.api.user.controller.request;

public record SaveEditProfilePageRequest(
        String profileUrl,
        String nickname,
        String serialId,
        String email,
        String password,
        String reconfirmPassword
) {
}
