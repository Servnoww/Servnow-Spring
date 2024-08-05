package servnow.servnow.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import servnow.servnow.api.dto.auth.AuthToken;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static servnow.servnow.auth.jwt.JwtValidator.BEARER_TYPE;

@Component
public class JwtGenerator {
    @Value("${jwt.secret}")
    private String JWT_SECRET;
//    @Value("${jwt.access-token-expiration}")
    @Value("1800000")
    private long ACCESS_TOKEN_EXPIRE_TIME;
//    @Value("${jwt.refresh-token-expiration}")
    @Value("604800000")
    private long REFRESH_TOKEN_EXPIRE_TIME;

    public static final String USER_ROLE_CLAIM_NAME = "role";

	private JwtProvider jwtTokenProvider;


/*    public String generateToken(String userId, String role, boolean isAccessToken) {
        final Date now = generateNowDate();
        final Date expiration = generateExpirationDate(isAccessToken, now);

        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
        if (isAccessToken) {
            claims.put(USER_ROLE_CLAIM_NAME, role);
        }

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }*/

    public AuthToken generate(String uid) {
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        //String subject = email.toString();
        String accessToken = jwtTokenProvider.accessTokenGenerate(uid, accessTokenExpiredAt);
        String refreshToken = jwtTokenProvider.refreshTokenGenerate(refreshTokenExpiredAt);

        return AuthToken.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }

    public JwtParser getJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build();
    }

    private Date generateNowDate() {
        return new Date();
    }

    private Date generateExpirationDate(boolean isAccessToken, Date now) {
        return new Date(now.getTime() + calculateExpirationTime(isAccessToken));
    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private long calculateExpirationTime(boolean isAccessToken) {
        if (isAccessToken) {
            return ACCESS_TOKEN_EXPIRE_TIME;
        }
        return REFRESH_TOKEN_EXPIRE_TIME;
    }
}