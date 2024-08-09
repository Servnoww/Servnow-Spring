package servnow.servnow.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import servnow.servnow.auth.UserAuthentication;
import servnow.servnow.auth.jwt.JwtProvider;
import servnow.servnow.auth.jwt.JwtValidator;
import servnow.servnow.common.code.AuthErrorCode;
import servnow.servnow.common.exception.UnauthorizedException;

import java.io.IOException;

import static servnow.servnow.auth.UserAuthentication.createUserAuthentication;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String BEARER = "Bearer ";
    private final JwtValidator jwtValidator;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("???? 으으하ㅏㅏ");
        String accessToken = getAccessToken(request);
        logger.info("???? 으으으ㅡ응");
        jwtValidator.validateAccessToken(accessToken);
        doAuthentication(request, jwtProvider.getSubject(accessToken));
        filterChain.doFilter(request, response);
    }

    // 헤더에서 액세스 토큰 추출
    private String getAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        System.out.println("header: "+accessToken);

        if (StringUtils.hasText(accessToken) && accessToken.startsWith(BEARER)) {
            return accessToken.substring(7);
        }
        throw new UnauthorizedException(AuthErrorCode.INVALID_ACCESS_TOKEN);
    }

    private void doAuthentication(
            HttpServletRequest request,
            Long userId) {
        UserAuthentication authentication = createUserAuthentication(userId);
        createAndSetWebAuthenticationDetails(request, authentication);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    private void createAndSetWebAuthenticationDetails(
            HttpServletRequest request,
            UserAuthentication authentication) {
        WebAuthenticationDetailsSource webAuthenticationDetailsSource = new WebAuthenticationDetailsSource();
        WebAuthenticationDetails webAuthenticationDetails = webAuthenticationDetailsSource.buildDetails(request);
        authentication.setDetails(webAuthenticationDetails);
    }
}