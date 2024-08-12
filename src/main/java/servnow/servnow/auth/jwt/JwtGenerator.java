package servnow.servnow.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtGenerator {
    @Value("${spring.jwt.secret.key}")
    private String JWT_SECRET;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60; 	//1시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14;  // 14일

    public static final String USER_ROLE_CLAIM_NAME = "role";

    public String generateToken(Long userId, String role, boolean isAccessToken) {
        final Date now = generateNowDate();
        final Date expiration = generateExpirationDate(isAccessToken, now);

        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
        if (isAccessToken) {
            claims.put(USER_ROLE_CLAIM_NAME, role);
        }
        logger.info("okay");
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }

    public JwtParser getJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
//                .setSigningKey(JWT_SECRET.getBytes())
                .build();
    }

    private Date generateNowDate() {
        return new Date();
    }

    private Date generateExpirationDate(boolean isAccessToken, Date now) {
        return new Date(now.getTime() + calculateExpirationTime(isAccessToken));
    }

    private Key getSigningKey() {
         try {
            byte[] keyBytes = Base64.getDecoder().decode(JWT_SECRET);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            logger.error("JWT_SECRET 값이 유효한 Base64 인코딩이 아닙니다: {}", e.getMessage());
            throw e;
        }
    }

    private long calculateExpirationTime(boolean isAccessToken) {
        if (isAccessToken) {
            return ACCESS_TOKEN_EXPIRE_TIME;
        }
        return REFRESH_TOKEN_EXPIRE_TIME;
    }
}