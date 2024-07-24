package servnow.servnow.api.tmp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import servnow.servnow.api.tmp.service.response.TmpGetResponse;

@Service
@RequiredArgsConstructor
public class TmpQueryService {

  public TmpGetResponse getTmp() {
    return TmpGetResponse.of("title");
  }
}
