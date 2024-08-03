package servnow.servnow.domain.subjectiveresult.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servnow.servnow.domain.subjectiveresult.model.SubjectiveResult;

public interface SubjectiveResultRepository extends JpaRepository<SubjectiveResult, Long> {
}