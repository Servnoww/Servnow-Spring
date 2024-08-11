package servnow.servnow.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthToken {
    private String accessToken;
    private String refreshToken;

    public static AuthToken of(String accessToken, String refreshToken) {
        return new AuthToken(accessToken, refreshToken);
    }
}
