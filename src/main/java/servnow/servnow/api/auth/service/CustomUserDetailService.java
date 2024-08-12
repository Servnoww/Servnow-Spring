package servnow.servnow.api.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.common.code.LoginErrorCode;
import servnow.servnow.domain.user.model.User;
import servnow.servnow.domain.user.repository.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String keyCode) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findBySerialId(keyCode)
                .orElseThrow(() -> new UsernameNotFoundException(String.valueOf(ServnowResponse.fail(LoginErrorCode.USER_NOT_FOUND))));
    }
}