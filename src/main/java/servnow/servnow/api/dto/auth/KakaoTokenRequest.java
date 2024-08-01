package servnow.servnow.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoTokenRequest {
    private String grantType;    // grant type (예: "authorization_code")
    private String clientId;     // 카카오 REST API 키
    private String redirectUri;  // 리디렉션 URI
    private String code;         // 인가 코드

    @Override
    public String toString() {
        return "grant_type=" + grantType +
               "&client_id=" + clientId +
               "&redirect_uri=" + redirectUri +
               "&code=" + code;
    }
}
