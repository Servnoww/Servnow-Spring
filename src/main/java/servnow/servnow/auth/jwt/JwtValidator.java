package servnow.servnow.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import servnow.servnow.common.code.AuthErrorCode;
import servnow.servnow.common.exception.UnauthorizedException;

import static servnow.servnow.auth.filter.JwtAuthenticationFilter.BEARER;

@RequiredArgsConstructor
@Component
public class JwtValidator {

    private final JwtGenerator jwtGenerator;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void validateAccessToken(String accessToken) {
        try {
            Claims claims = parseToken(accessToken);

            String role = claims.get(JwtGenerator.USER_ROLE_CLAIM_NAME, String.class);
            if (role == null) {
                throw new UnauthorizedException(AuthErrorCode.INVALID_ACCESS_TOKEN_VALUE);
            }
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(AuthErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (JwtException e) {
            throw new UnauthorizedException(AuthErrorCode.INVALID_ACCESS_TOKEN_VALUE);
        } catch (Exception e) {
            throw new UnauthorizedException(AuthErrorCode.INVALID_ACCESS_TOKEN_VALUE);
        }
    }


    public void validateRefreshToken(final String refreshToken) {
        try {
            System.out.println("refreshToken" + refreshToken);
            parseToken(getToken(refreshToken));
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(AuthErrorCode.EXPIRED_REFRESH_TOKEN);
        } catch (Exception e) {
            throw new UnauthorizedException(AuthErrorCode.INVALID_REFRESH_TOKEN_VALUE);
        }
    }

    public void equalsRefreshToken(
            final String refreshToken,
            final String storedRefreshToken) {
        if (!getToken(refreshToken).equals(storedRefreshToken)) {
            throw new UnauthorizedException(AuthErrorCode.MISMATCH_REFRESH_TOKEN);
        }
    }

    private String getToken(final String refreshToken) {
        if (refreshToken.startsWith(BEARER)) {
            return refreshToken.substring(BEARER.length());
        }
        throw new UnauthorizedException(AuthErrorCode.MISSING_BEARER_PREFIX);
    }

    private Claims parseToken(final String token) {
        return jwtGenerator.getJwtParser().parseClaimsJws(token).getBody();
    }
}