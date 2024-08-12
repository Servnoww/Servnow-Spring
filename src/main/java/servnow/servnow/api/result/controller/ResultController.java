package servnow.servnow.api.result.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.api.result.dto.request.ResultPostRequest;
import servnow.servnow.api.result.service.ResultCommandService;
import servnow.servnow.api.result.service.ResultQueryService;
import servnow.servnow.auth.UserId;
import servnow.servnow.common.code.CommonSuccessCode;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ResultController {
  private final ResultCommandService resultCommandService;
  private final ResultQueryService resultQueryService;

  @PostMapping("/result/{surveyId}")
  public ServnowResponse<Void> createResult(@Parameter(hidden = true) @UserId Long userId, @RequestBody @Valid ResultPostRequest resultPostRequest, @PathVariable @Valid Long surveyId) {
    resultCommandService.createResult(userId, resultPostRequest, surveyId);
    return ServnowResponse.success(CommonSuccessCode.CREATED);
  }

  @PostMapping("/result/guest/{surveyId}")
  public ServnowResponse<Void> createResultForGuest(@RequestBody @Valid ResultPostRequest resultPostRequest, @PathVariable @Valid Long surveyId) {
    resultCommandService.createResultForGuest(resultPostRequest, surveyId);
    return ServnowResponse.success(CommonSuccessCode.CREATED);
  }
}