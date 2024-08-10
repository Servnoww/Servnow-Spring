package servnow.servnow.api.tmp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import servnow.servnow.domain.tmp.repository.TmpRepository;

@Service
@RequiredArgsConstructor
public class TmpCommandService {

  private final TmpRepository tmpRepository;

  public void createTmp() {
  }

}
