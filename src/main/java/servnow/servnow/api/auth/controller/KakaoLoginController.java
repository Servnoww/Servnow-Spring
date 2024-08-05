//package servnow.servnow.api.auth.controller;
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//import servnow.servnow.api.auth.service.KakaoService;
//import servnow.servnow.api.dto.ServnowResponse;
//import servnow.servnow.api.dto.auth.AuthToken;
//
//import java.util.NoSuchElementException;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1")
//public class KakaoLoginController {
//
//    private final KakaoService kakaoService;
//
//    //web 버전
//    @ResponseBody
//    @GetMapping("/auth/kakao")
//    public ServnowResponse<AuthToken> kakaoLogin(@RequestParam String code, HttpServletRequest request) {
//        try {
//            return ServnowResponse.success(kakaoService.kakaoLogin(code));
//        } catch (NoSuchElementException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item Not Found");
//        }
//    }
//}