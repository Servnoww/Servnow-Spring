package servnow.servnow.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servnow.servnow.domain.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySerialId(String serialId);
}