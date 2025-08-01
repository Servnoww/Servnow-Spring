package servnow.servnow.api.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final long EXPIRATION_TIME = 5 * 60; // 5분

    public void saveVerificationCode(String email, String code) {
        redisTemplate.opsForValue().set(buildKey(email), code, EXPIRATION_TIME, TimeUnit.SECONDS);
    }

    public boolean verifyCode(String email, String inputCode) {
        String storedCode = redisTemplate.opsForValue().get(buildKey(email));
        return storedCode != null && storedCode.equals(inputCode);
    }

    private String buildKey(String email) {
        return "email:verification:" + email;
    }
}
