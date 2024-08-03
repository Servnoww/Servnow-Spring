package servnow.servnow.api.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.common.code.UserErrorCode;
import servnow.servnow.common.exception.NotFoundException;
import servnow.servnow.domain.user.model.User;
import servnow.servnow.domain.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserFinder {

  private final UserRepository userRepository;

  public User findById(final long id) {
    return userRepository.findById(id).orElseThrow(() -> new NotFoundException(UserErrorCode.USER_NOT_FOUND));
  }
}
