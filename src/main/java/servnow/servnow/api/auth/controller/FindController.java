package servnow.servnow.api.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import servnow.servnow.api.auth.service.FindInfoService;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.common.code.CommonSuccessCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FindController {
    private FindInfoService findInfoService;

    @GetMapping("/auth/find/id")
    public ServnowResponse<String> findId(Authentication authentication) {
        String username = findInfoService.findSerialId(authentication);

        return ServnowResponse.success(CommonSuccessCode.OK, username);
    }
}
