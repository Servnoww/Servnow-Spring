package servnow.servnow.api.survey.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import servnow.servnow.api.question.service.MultipleChoiceUpdater;
import servnow.servnow.api.question.service.QuestionUpdater;
import servnow.servnow.api.section.service.SectionUpdater;
import servnow.servnow.api.survey.dto.SurveyPostRequest;
import servnow.servnow.api.survey.dto.SurveyPostRequest.SurveyPostSection;
import servnow.servnow.api.survey.dto.SurveyPostRequest.SurveyPostSection.SurveyPostQuestion;
import servnow.servnow.api.survey.dto.SurveyPostRequest.SurveyPostSection.SurveyPostQuestion.SurveyPostAnswer;
import servnow.servnow.api.user.service.UserFinder;
import servnow.servnow.domain.question.model.MultipleChoice;
import servnow.servnow.domain.question.model.Question;
import servnow.servnow.domain.question.model.enums.QuestionType;
import servnow.servnow.domain.section.model.Section;
import servnow.servnow.domain.survey.model.Survey;

@Service
@RequiredArgsConstructor
public class SurveyCommandService {

  private final SurveyUpdater surveyUpdater;
  private final SectionUpdater sectionUpdater;
  private final UserFinder userFinder;
  private final MultipleChoiceUpdater multipleChoiceUpdater;
  private final QuestionUpdater questionUpdater;

  @Transactional
  public void createSurvey(final long userId, final SurveyPostRequest surveyPostRequest) {

    Survey survey = surveyUpdater.save(surveyPostRequest.toEntity(userFinder.findById(userId)));

    List<Question> mapToQuestions = new ArrayList<>();
    List<MultipleChoice> mapToMultipleChoices = new ArrayList<>();
;
    sectionUpdater.saveAll(convertToSections(survey, surveyPostRequest.sections(), mapToQuestions, mapToMultipleChoices));
    questionUpdater.saveAll(mapToQuestions);
    multipleChoiceUpdater.saveAll(mapToMultipleChoices);
  }

  private List<Section> convertToSections(final Survey survey, final List<SurveyPostSection> surveyPostSection, final List<Question> mapToQuestions, final List<MultipleChoice> mapToMultipleChoices) {
    return surveyPostSection.stream().map(s ->{
      Section section = s.toEntity(survey);
      mapToQuestions.addAll(convertToQuestions(section, s.questions(), mapToMultipleChoices));
      return section;
    }).toList();
  }

  private List<Question> convertToQuestions(final Section section, final List<SurveyPostQuestion> surveyPostQuestions, final List<MultipleChoice> mapToMultipleChoices) {
    return surveyPostQuestions.stream().map(q -> {
      Question question = q.toEntity(section);
      if (q.questionType().equals(QuestionType.MULTIPLE_CHOICE)) {
        mapToMultipleChoices.addAll(convertToMultipleChoices(question, q.answers()));
      }
      return question;
    }).toList();
  }

  private List<MultipleChoice> convertToMultipleChoices(final Question question, final List<SurveyPostAnswer> surveyPostAnswers) {
      return surveyPostAnswers.stream().map(it -> it.toEntity(question)).toList();
  }
}