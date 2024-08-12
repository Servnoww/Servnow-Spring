package servnow.servnow.api.dto.login;

import jakarta.annotation.Nullable;

public record UserChangePwRequest (
        @Nullable
        String password,

        @Nullable
        String repassword
){}
