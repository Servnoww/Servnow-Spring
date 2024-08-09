package servnow.servnow.api.dto.login;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
        @Nullable
        String name,
        @NotBlank
        String platform
){}