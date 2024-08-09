package servnow.servnow.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servnow.servnow.domain.user.model.User;
import servnow.servnow.domain.user.model.enums.Platform;
import servnow.servnow.domain.user.model.enums.UserStatus;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findBySerialId(String id);
    Optional<User> findByPlatformAndSerialId(Platform platform, String serialId);
    boolean existsByPlatformAndSerialIdAndStatus(Platform platform, String id, UserStatus userStatus);

}