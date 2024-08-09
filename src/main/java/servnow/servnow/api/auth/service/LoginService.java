package servnow.servnow.api.auth.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import servnow.servnow.api.dto.login.UserLoginRequest;
import servnow.servnow.api.dto.login.UserLoginResponse;
import servnow.servnow.api.user.service.UserInfoFinder;
import servnow.servnow.auth.jwt.JwtProvider;
import servnow.servnow.auth.jwt.Token;
import servnow.servnow.domain.user.model.User;
import servnow.servnow.domain.user.model.UserInfo;
import servnow.servnow.domain.user.model.enums.Platform;
import servnow.servnow.domain.user.repository.UserInfoRepository;
import servnow.servnow.domain.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static servnow.servnow.domain.user.model.User.createUser;

@Service
@RequiredArgsConstructor
public class LoginService {
	private final UserRepository userRepository;
	private final UserInfoRepository userInfoRepository;
	private final JwtProvider jwtProvider;
	private final UserInfoFinder userInfoFinder;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// 일반 로그인 메서드
	public UserLoginResponse login(UserLoginRequest request) {
        logger.info("안녕!");
		Optional<User> optionalUser = userRepository.findBySerialId(request.serialId());
		if (optionalUser.isEmpty()) {
			throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
		}
        logger.info("안녕!ㅎㅎ");

		User user = optionalUser.get();

		// 비밀번호 검증
//		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
//			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//		}

		// 토큰 생성
		Token issuedToken = jwtProvider.issueTokens(user.getId(), user.getUserRole().getValue());
		UserInfo userInfo = userInfoFinder.findByUserId(user.getId());
		userInfo.setRefreshToken(issuedToken.refreshToken());

		return UserLoginResponse.of(issuedToken, true);
	}

	// 회원가입 메서드
	public Token register(UserLoginRequest request) {

		Optional<User> existingUser = userRepository.findBySerialId(request.serialId());
		if (existingUser.isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
		}

		// 비밀번호 암호화
//		String encodedPassword = passwordEncoder.encode(request.password());

		User newUser = createUser(
				request.email(),
				Platform.SERVNOW
		);
		User.builder().password(request.password()).build();
		userRepository.save(newUser);

		UserInfo newUserInfo = UserInfo.createMemberInfo(
				newUser, null, request.nickname(), String.valueOf(request.gender()), request.email(), LocalDate.now(), "default_url");
		userInfoRepository.save(newUserInfo);

		// JWT 토큰 생성
		return jwtProvider.issueTokens(newUser.getId(), newUser.getUserRole().getValue());
	}
}
