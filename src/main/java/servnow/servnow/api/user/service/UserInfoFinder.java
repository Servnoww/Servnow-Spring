package servnow.servnow.api.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.domain.user.model.UserInfo;
import servnow.servnow.domain.user.repository.UserInfoRepository;

@RequiredArgsConstructor
@Component
public class UserInfoFinder {

  private final UserInfoRepository userInfoRepository;

  public UserInfo findByUserId(final long userId){
    return userInfoRepository.findByUserId(userId).orElseThrow();
  }

  public boolean isEmailDuplicate(String email) {
    return userInfoRepository.existsByEmail(email);
  }

}
