package servnow.servnow.api.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import servnow.servnow.api.auth.service.AuthService;
import servnow.servnow.api.tmp.service.TmpCommandService;
import servnow.servnow.api.tmp.service.TmpQueryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

  private final AuthService authService;

}
