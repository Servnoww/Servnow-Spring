package servnow.servnow.api.auth.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KakaoAuthApi {
    @Value("${kakao.api_key}")
//    @Value("b6abc922f0349ff86a6d90bee11a8091")
    private String kakaoApiKey;

    @Value("${kakao.redirect_uri}")
//    @Value("http://localhost:8080/api/v1/auth/kakao")
    private String kakaoRedirectUri;
}
