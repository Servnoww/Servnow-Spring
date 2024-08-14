package servnow.servnow.api.auth.dto.response;

import servnow.servnow.auth.jwt.Token;

public record ReissueTokenResponse(
    String accessToken,
    String refreshToken
    ) {

  public static ReissueTokenResponse of(Token token) {
    return new ReissueTokenResponse(token.accessToken(), token.refreshToken());
  }
}
