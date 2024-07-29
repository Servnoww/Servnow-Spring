package servnow.servnow.domain.section.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servnow.servnow.domain.section.model.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {

}
