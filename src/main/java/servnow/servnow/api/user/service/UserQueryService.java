package servnow.servnow.api.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.api.user.dto.response.EditProfilePageResponse;
import servnow.servnow.api.user.dto.response.KakaoEditProfilePageResponse;
import servnow.servnow.api.user.dto.response.MyPageResponse;
import servnow.servnow.common.code.CommonSuccessCode;
import servnow.servnow.common.code.UserErrorCode;
import servnow.servnow.common.code.UserInfoErrorCode;
import servnow.servnow.common.exception.NotFoundException;
import servnow.servnow.domain.user.model.User;
import servnow.servnow.domain.user.model.UserInfo;
import servnow.servnow.api.user.dto.response.ServnowEditProfilePageResponse;
import servnow.servnow.domain.user.model.enums.Platform;
import servnow.servnow.domain.user.repository.UserInfoRepository;
import servnow.servnow.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;
    private final UserInfoFinder userInfoFinder;
    private final EmailService emailService;

    @Transactional(readOnly = true)
    public MyPageResponse getMyPage() {
        User user = userRepository.findById(3L)
                .orElseThrow(() -> new NotFoundException(UserErrorCode.USER_NOT_FOUND));
        UserInfo userinfo = userInfoRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(UserErrorCode.USER_NOT_FOUND));
        return MyPageResponse.of(userinfo, user);
    }

    public EditProfilePageResponse getEditProfilePage() {
        User user = userRepository.findById(2L)
                .orElseThrow(() -> new NotFoundException(UserErrorCode.USER_NOT_FOUND));
        UserInfo userInfo = userInfoRepository.findById(2L)
                .orElseThrow(() -> new NotFoundException(UserInfoErrorCode.USER_NOT_FOUND));

        if (user.getPlatform() == Platform.KAKAO) {
            return KakaoEditProfilePageResponse.of(userInfo);
        } else if (user.getPlatform() == Platform.SERVNOW) {
            return ServnowEditProfilePageResponse.of(userInfo, user);
        } else {
            throw new NotFoundException(UserErrorCode.PLATFORM_NOT_FOUND);
        }
    }

    public boolean emailDuplicate(String email) {
        return userInfoFinder.isEmailDuplicate(email);
    }

    public boolean getSerialIdDuplicate(String serialId) {
        return userRepository.existsBySerialId(serialId);
    }

    public ServnowResponse<Void> identityVerification(String email) throws Exception {
        if (emailDuplicate(email)) {
            return ServnowResponse.fail(UserErrorCode.EMAIL_DUPLICATE);
        }
        String confirm = emailService.sendSimpleMessage(email);

        if (confirm.isEmpty()) {
            return ServnowResponse.fail(UserErrorCode.SEND_CERTIFICATION_NUMBER);
        } else {
            return ServnowResponse.success(CommonSuccessCode.OK);
        }
    }
}
