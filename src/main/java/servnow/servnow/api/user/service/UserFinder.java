package servnow.servnow.api.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.domain.user.model.User;
import servnow.servnow.domain.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserFinder {

  private final UserRepository userRepository;

  public User findById(final long id) {
    // 머지되면 예외처리 추가
    return userRepository.findById(id).orElseThrow();
  }
}
