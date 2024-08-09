package servnow.servnow.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtGenerator {
    @Value("${jwt.secret}")
    private String JWT_SECRET;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60; 	//1시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14;  // 14일

    public static final String USER_ROLE_CLAIM_NAME = "role";

    public JwtParser getJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build();
    }

    private Key getSigningKey() {
//        byte[] keyBytes = Base64.getUrlDecoder().decode(JWT_SECRET);
//        return Keys.hmacShaKeyFor(keyBytes);
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    private long calculateExpirationTime(boolean isAccessToken) {
        if (isAccessToken) {
            return ACCESS_TOKEN_EXPIRE_TIME;
        }
        return REFRESH_TOKEN_EXPIRE_TIME;
    }

    public String generateToken(Long serialId, String role, boolean isAccessToken) {
        final Date now = generateNowDate();
        final Date expiration = generateExpirationDate(isAccessToken, now);

        Claims claims = Jwts.claims().setSubject(String.valueOf(serialId));
        if (isAccessToken) {
            claims.put(USER_ROLE_CLAIM_NAME, role);
        }

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Date generateNowDate() {
        return new Date();
    }

    private Date generateExpirationDate(boolean isAccessToken, Date now) {
        return new Date(now.getTime() + calculateExpirationTime(isAccessToken));
    }
}