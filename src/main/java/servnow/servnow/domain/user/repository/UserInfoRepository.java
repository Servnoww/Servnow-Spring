package servnow.servnow.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import servnow.servnow.domain.user.model.UserInfo;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

  Optional<UserInfo> findByUserId(long userId);

  @Modifying
  @Query("update UserInfo u set u.point = u.point + :increment where u.id = :id")
  void updatePointById(@Param("increment") int increment, @Param("id") long id);

    boolean existsByEmail(String email);
}