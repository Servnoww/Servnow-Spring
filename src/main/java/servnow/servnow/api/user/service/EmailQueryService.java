package servnow.servnow.api.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailQueryService {

    private final EmailVerificationService emailVerificationService;

    public boolean verifyCode(String email, String inputCode) {
        return emailVerificationService.verifyCode(email, inputCode);
    }
}
