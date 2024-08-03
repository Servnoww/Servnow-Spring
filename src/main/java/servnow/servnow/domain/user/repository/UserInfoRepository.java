package servnow.servnow.domain.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import servnow.servnow.domain.user.model.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
  Optional<UserInfo> findByUserId(long userId);
}