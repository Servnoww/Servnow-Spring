package servnow.servnow.api.user.dto.request;

public record SaveEditProfilePageRequest(
//        String profileUrl,
        String serialId,
        String email,
        String certificationNumber,
        String password,
        String reconfirmPassword
) {
}
