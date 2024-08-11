package servnow.servnow.api.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.domain.user.model.User;
import servnow.servnow.domain.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserUpdater {

    private final UserRepository userRepository;

    public void saveUser(final User user) {
        userRepository.save(user);
    }
}