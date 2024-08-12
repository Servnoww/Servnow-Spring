package servnow.servnow.api.dto.login;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import servnow.servnow.domain.user.model.enums.Gender;

import java.time.LocalDate;

public record UserLoginRequest(
        @Nullable
        String serialId,
        @Nullable
        String password
){}