package servnow.servnow.api.tmp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.api.tmp.service.TmpCommandService;
import servnow.servnow.api.tmp.service.TmpQueryService;
import servnow.servnow.api.tmp.service.response.TmpGetResponse;
import servnow.servnow.common.code.CommonSuccessCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TmpController {

  private final TmpCommandService tmpCommandService;
  private final TmpQueryService tmpQueryService;

  // 리소스 생성 응답 예시
  @PostMapping("/tmp")
  public ServnowResponse<Void> createTmp() {
    tmpCommandService.createTmp();
    return ServnowResponse.success(CommonSuccessCode.CREATED);
  }

  // 조회 응답 예시
  @GetMapping("/tmp")
  public ServnowResponse<TmpGetResponse> getTmp() {
    return ServnowResponse.success(CommonSuccessCode.OK, tmpQueryService.getTmp());
  }
}