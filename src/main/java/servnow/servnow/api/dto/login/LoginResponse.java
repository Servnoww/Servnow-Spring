package servnow.servnow.api.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import servnow.servnow.api.dto.auth.AuthToken;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String serialId;
    private AuthToken token;
}
