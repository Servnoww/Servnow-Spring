package servnow.servnow.domain.s3.uuid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servnow.servnow.domain.s3.uuid.model.Uuid;

public interface UuidRepository extends JpaRepository<Uuid, Long> {
}
