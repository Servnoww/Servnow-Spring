package servnow.servnow.api.result.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import servnow.servnow.domain.subjectiveresult.model.SubjectiveResult;
import servnow.servnow.domain.subjectiveresult.repository.SubjectiveResultRepository;

@Component
@RequiredArgsConstructor
public class SubjectiveResultUpdater {

    private final SubjectiveResultRepository subjectiveResultRepository;

    public void save(final SubjectiveResult subjectResult) {subjectiveResultRepository.save(subjectResult);}

}
