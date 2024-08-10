package servnow.servnow.api.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.domain.user.repository.UserInfoRepository;

@RequiredArgsConstructor
@Component
public class UserInfoUpdater {

  private final UserInfoRepository userInfoRepository;

  public void updatePointById(final int increment, final long id){
    userInfoRepository.updatePointById(increment, id);
  }
}