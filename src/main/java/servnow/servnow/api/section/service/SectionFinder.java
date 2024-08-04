package servnow.servnow.api.section.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.common.code.SectionErrorCode;
import servnow.servnow.common.exception.NotFoundException;
import servnow.servnow.domain.section.model.Section;
import servnow.servnow.domain.section.repository.SectionRepository;

@Component
@RequiredArgsConstructor
public class SectionFinder {

  private final SectionRepository sectionRepository;

  public Section findBySurveyIdAndSectionOrderWithQuestions(final long surveyId, final int sectionOrder) {
    return sectionRepository.findBySurveyIdAndSectionOrderWithQuestions(surveyId, sectionOrder).orElseThrow(() -> new NotFoundException(SectionErrorCode.SECTION_NOT_FOUND));
  }

  public long countBySurvey(final long surveyId) {
    return sectionRepository.countBySurveyId(surveyId);
  }
}