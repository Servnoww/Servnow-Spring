package servnow.servnow.api.dto.login;

import jakarta.annotation.Nullable;

import java.time.LocalDate;

public record UserJoinRequest(
        @Nullable
        String serialId,
        @Nullable
        String password,
        @Nullable
        String repassword,
        @Nullable
        String email,
        @Nullable
        String nickname,
        @Nullable
        String gender,
        @Nullable
        LocalDate birth
){}