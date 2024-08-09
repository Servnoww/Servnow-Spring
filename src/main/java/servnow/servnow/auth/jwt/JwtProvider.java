package servnow.servnow.auth.jwt;

import io.jsonwebtoken.JwtParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {
    private final JwtGenerator jwtGenerator;
    public JwtProvider(JwtGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }


    public Token issueTokens(Long userId, String role) {
        return Token.of(jwtGenerator.generateToken(userId, role, true),
                jwtGenerator.generateToken(userId, role, false));
    }

    public Long getSubject(String token) {
        JwtParser jwtParser = jwtGenerator.getJwtParser();
        return Long.valueOf(jwtParser.parseClaimsJws(token)
                .getBody()
                .getSubject());
    }
}