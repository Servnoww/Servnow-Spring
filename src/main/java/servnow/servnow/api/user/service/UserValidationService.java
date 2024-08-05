package servnow.servnow.api.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import servnow.servnow.domain.user.repository.UserInfoRepository;

@Service
@RequiredArgsConstructor
public class UserValidationService {
    private final UserInfoRepository userInfoRepository;

    public boolean isEmailDuplicate(String email) {
        return userInfoRepository.existsByEmail(email);
    }
}