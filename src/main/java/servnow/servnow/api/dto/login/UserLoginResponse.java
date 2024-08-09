package servnow.servnow.api.dto.login;

import lombok.*;
import servnow.servnow.api.dto.auth.AuthToken;
import servnow.servnow.auth.jwt.Token;

@Builder(access = AccessLevel.PRIVATE)
public record UserLoginResponse(
        String accessToken,
        String refreshToken,
        boolean isRegistered

) {
    public static UserLoginResponse of(Token token, boolean isRegistered) {
        return UserLoginResponse.builder()
                .accessToken(token.accessToken())
                .refreshToken(token.refreshToken())
                .isRegistered(isRegistered)
                .build();
    }
}