package servnow.servnow.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import servnow.servnow.auth.JwtAuthenticationEntryPoint;
import servnow.servnow.auth.filter.ExceptionHandlerFilter;
import servnow.servnow.auth.filter.JwtAuthenticationFilter;
import servnow.servnow.auth.jwt.JwtProvider;
import servnow.servnow.auth.jwt.JwtValidator;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtValidator jwtValidator;
    private final JwtProvider jwtProvider;

    private static final String[] whiteList = { "/api/v1/auth/login", "/api/v1/auth/join", "/api/v1/auth/kakao", "/api/v1/find/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/api/v1/survey/guest/**", "/api/v1/survey/home", "/api/v1/survey", "api/v1/result/guest/**", "/api/v1/change/pw"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .anyRequest()
                                .authenticated())
                .addFilterBefore(new JwtAuthenticationFilter(jwtValidator, jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(), JwtAuthenticationFilter.class)
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(whiteList);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}