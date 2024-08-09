package servnow.servnow.api.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.common.code.LoginErrorCode;
import servnow.servnow.common.exception.NotFoundException;
import servnow.servnow.domain.user.model.UserInfo;
import servnow.servnow.domain.user.repository.UserInfoRepository;

@Component
@RequiredArgsConstructor
public class UserInfoFinder {

    private final UserInfoRepository userInfoRepository;

    public UserInfo getUserInfo(final Long userId) {
        return userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(LoginErrorCode.USER_NOT_FOUND));
    }
}