package servnow.servnow.api.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import servnow.servnow.domain.user.model.UserInfo;
import servnow.servnow.domain.user.repository.UserInfoRepository;

@Service
@RequiredArgsConstructor
public class FindInfoService {

    private final UserInfoRepository userInfoRepository;

    /**
     * 아이디 반환
     */
    public String findSerialId(Authentication authentication) {
        String email = authentication.getName();
        UserInfo userInfo = userInfoRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userInfo.getUser().getSerialId();
    }
}
