package servnow.servnow.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servnow.servnow.domain.user.model.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    boolean existsByEmail(String email);
}