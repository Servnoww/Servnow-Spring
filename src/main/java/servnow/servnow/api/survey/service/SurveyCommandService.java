package servnow.servnow.api.survey.service;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import servnow.servnow.api.question.service.MultipleChoiceUpdater;
import servnow.servnow.api.question.service.QuestionUpdater;
import servnow.servnow.api.section.service.SectionUpdater;
import servnow.servnow.api.survey.dto.request.SurveyPostRequest;
import servnow.servnow.api.survey.dto.request.SurveyPostRequest.SurveyPostSection;
import servnow.servnow.api.survey.dto.request.SurveyPostRequest.SurveyPostSection.SurveyPostQuestion;
import servnow.servnow.api.survey.dto.request.SurveyPostRequest.SurveyPostSection.SurveyPostQuestion.SurveyPostAnswer;
import servnow.servnow.api.user.service.UserFinder;
import servnow.servnow.api.user.service.UserInfoFinder;
import servnow.servnow.api.user.service.UserInfoUpdater;
import servnow.servnow.domain.question.model.Question;
import servnow.servnow.domain.question.model.enums.QuestionType;
import servnow.servnow.domain.section.model.Section;
import servnow.servnow.domain.survey.model.Survey;
import servnow.servnow.domain.user.model.UserInfo;

@Service
@RequiredArgsConstructor
public class SurveyCommandService {

  private final UserFinder userFinder;
  private final UserInfoFinder userInfoFinder;
  private final SurveyUpdater surveyUpdater;
  private final SectionUpdater sectionUpdater;
  private final MultipleChoiceUpdater multipleChoiceUpdater;
  private final QuestionUpdater questionUpdater;
  private final UserInfoUpdater userInfoUpdater;

  @Transactional
  public void createSurvey(final long userId, final SurveyPostRequest surveyPostRequest) {
    Survey survey = surveyUpdater.save(surveyPostRequest.toEntity(userFinder.findById(userId)));
    saveSectionsAndQuestions(survey, surveyPostRequest.sections());
    updateUserPoint(userId);
  }

  private void updateUserPoint(final long userId) {
    UserInfo userInfo = userInfoFinder.findByUserId(userId);
    // userInfo.incrementPoint(200);
    userInfoUpdater.updatePointById(200, userInfo.getId());
  }

  public void saveSectionsAndQuestions(final Survey survey, final List<SurveyPostSection> surveyPostSections) {
    surveyPostSections.forEach(s -> {
      Section section = s.toEntity(survey);
      sectionUpdater.save(section);
      saveQuestions(section, s.questions());
    });
  }

  public void saveQuestions(final Section section, final List<SurveyPostQuestion> surveyPostQuestions) {
    surveyPostQuestions.forEach(q -> {
      Question question = q.toEntity(section);
      questionUpdater.save(question);
      if (q.questionType().equals(QuestionType.MULTIPLE_CHOICE)) {
        saveMultipleChoices(question, q.answers());
      }
    });
  }

  public void saveMultipleChoices(final Question question, final List<SurveyPostAnswer> surveyPostAnswers) {
    surveyPostAnswers.stream()
        .map(answerDto -> answerDto.toEntity(question))
        .forEach(multipleChoiceUpdater::save);
  }
}