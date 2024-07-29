package servnow.servnow.api.result.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import servnow.servnow.api.result.service.ResultCommandService;
import servnow.servnow.api.result.service.ResultQueryService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ResultController {
  private final ResultCommandService resultCommandService;
  private final ResultQueryService resultQueryService;
}