package servnow.servnow.api.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import servnow.servnow.api.user.service.response.EditProfilePageResponse;
import servnow.servnow.api.user.service.response.KakaoEditProfilePageResponse;
import servnow.servnow.api.user.controller.response.MyPageResponse;
import servnow.servnow.common.code.UserErrorCode;
import servnow.servnow.common.exception.NotFoundException;
import servnow.servnow.domain.user.model.User;
import servnow.servnow.domain.user.model.UserInfo;
import servnow.servnow.api.user.service.response.ServnowEditProfilePageResponse;
import servnow.servnow.common.code.UserMyPageErrorCode;
import servnow.servnow.domain.user.model.enums.Platform;
import servnow.servnow.domain.user.repository.UserInfoRepository;
import servnow.servnow.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public MyPageResponse getMyPage() {
        User user = userRepository.findById(3L)
                .orElseThrow(() -> new NotFoundException(UserErrorCode.USER_NOT_FOUND));
        UserInfo userinfo = userInfoRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(UserErrorCode.USER_NOT_FOUND));
        return MyPageResponse.of(userinfo, user);
    }

    public EditProfilePageResponse getEditProfilePage() {
        User user = userRepository.findById(4L)
                .orElseThrow(() -> new NotFoundException(UserMyPageErrorCode.USER_NOT_FOUND));
        UserInfo userInfo = userInfoRepository.findById(4L)
                .orElseThrow(() -> new NotFoundException(UserMyPageErrorCode.USER_NOT_FOUND));

        if (user.getPlatform() == Platform.KAKAO) {
            return KakaoEditProfilePageResponse.of(userInfo);
        } else if (user.getPlatform() == Platform.SERVNOW) {
            return ServnowEditProfilePageResponse.of(userInfo, user);
        } else {
            throw new NotFoundException(UserMyPageErrorCode.PLATFORM_NOT_FOUND);
        }
    }

    public boolean emailDuplicate(String email) {
        return userInfoRepository.existsByEmail(email);
    }
}
