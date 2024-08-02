package servnow.servnow.api.section.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.domain.section.model.Section;
import servnow.servnow.domain.section.repository.SectionRepository;

@Component
@RequiredArgsConstructor
public class SectionUpdater {

  private final SectionRepository sectionRepository;

  public void save(final Section section) {
    sectionRepository.save(section);
  }
}
