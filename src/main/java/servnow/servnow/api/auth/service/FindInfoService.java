package servnow.servnow.api.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import servnow.servnow.api.dto.ServnowResponse;
import servnow.servnow.api.user.service.EmailService;
import servnow.servnow.common.code.CommonSuccessCode;
import servnow.servnow.common.code.LoginErrorCode;
import servnow.servnow.common.code.UserErrorCode;
import servnow.servnow.domain.user.model.User;
import servnow.servnow.domain.user.model.UserInfo;
import servnow.servnow.domain.user.repository.UserInfoRepository;
import servnow.servnow.domain.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindInfoService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 이메일 존재 여부 확인
     */
     public String checkEmail(String email) throws Exception {
        Optional<UserInfo> userInfo = userInfoRepository.findByEmail(email);
        if (userInfo.isPresent()) {
            String confirm = emailService.sendSimpleMessage(email);
            String serialId = userInfo.get().getUser().getSerialId();

            if (confirm.isEmpty()) {
                return UserErrorCode.SEND_CERTIFICATION_NUMBER.getMessage();
            } else {
                return serialId;
            }
        } else {
                return LoginErrorCode.USER_NOT_FOUND.getMessage();
        }
    }


    public String updatePassword(String serialId, String password) throws Exception {
        User user = userRepository.findBySerialId(serialId)
                .orElseThrow(() -> new Exception(LoginErrorCode.USER_NOT_FOUND.getMessage()));

         if (isValidPassword(password)) {
            String encodedPassword = passwordEncoder.encode(password);

            user.setPassword(encodedPassword);
            userRepository.save(user);

            return CommonSuccessCode.OK.getMessage();
         } else {
                return LoginErrorCode.INVALID_PASSWORD_FORMAT.getMessage();
         }

    }

    // 비밀번호 유효성 검증
	private boolean isValidPassword(String password) {
		String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,20}$";
		return password.matches(regex);
	}
}
