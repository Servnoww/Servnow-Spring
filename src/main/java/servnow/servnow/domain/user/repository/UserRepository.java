package servnow.servnow.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servnow.servnow.domain.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
