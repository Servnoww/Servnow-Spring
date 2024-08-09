package servnow.servnow.api.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.common.code.LoginErrorCode;
import servnow.servnow.common.exception.NotFoundException;
import servnow.servnow.domain.user.model.User;
import servnow.servnow.domain.user.model.enums.Platform;
import servnow.servnow.domain.user.repository.UserRepository;

import java.util.Optional;

import static servnow.servnow.domain.user.model.enums.UserStatus.ACTIVE;

@Component
@RequiredArgsConstructor
public class UserFinder {

    private final UserRepository userRepository;

    public User getUser(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(LoginErrorCode.USER_NOT_FOUND));
    }

    public User getUserReference(final Long id) {
        return userRepository.getReferenceById(id);
    }

    public boolean isRegisteredUser(final Platform platform, final String serialId) {
        return userRepository.existsByPlatformAndSerialIdAndStatus(
                platform,
                serialId,
                ACTIVE);
    }

    public Optional<User> findUserByPlatFormAndSeralId(final Platform platform, final String serialId) {
        return userRepository.findByPlatformAndSerialId(platform, serialId);
    }
}