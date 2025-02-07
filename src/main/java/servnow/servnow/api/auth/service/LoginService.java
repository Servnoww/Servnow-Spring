package servnow.servnow.api.auth.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import servnow.servnow.api.auth.dto.response.ReissueTokenResponse;
import servnow.servnow.api.dto.login.UserJoinRequest;
import servnow.servnow.api.dto.login.UserLoginRequest;
import servnow.servnow.api.dto.login.UserLoginResponse;
import servnow.servnow.api.user.service.UserFinder;
import servnow.servnow.api.user.service.UserInfoFinder;
import servnow.servnow.auth.jwt.JwtProvider;
import servnow.servnow.auth.jwt.JwtValidator;
import servnow.servnow.auth.jwt.Token;
import servnow.servnow.common.code.LoginErrorCode;
import servnow.servnow.common.exception.BadRequestException;
import servnow.servnow.common.exception.UnauthorizedException;
import servnow.servnow.domain.user.model.User;
import servnow.servnow.domain.user.model.UserInfo;
import servnow.servnow.domain.user.model.enums.Gender;
import servnow.servnow.domain.user.model.enums.Platform;
import servnow.servnow.domain.user.repository.UserInfoRepository;
import servnow.servnow.domain.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static servnow.servnow.auth.filter.JwtAuthenticationFilter.BEARER;
import static servnow.servnow.domain.user.model.User.createUser;


@Service
@RequiredArgsConstructor
public class LoginService {

	private final UserRepository userRepository;
	private final UserInfoRepository userInfoRepository;
	private final JwtProvider jwtProvider;
	private final UserInfoFinder userInfoFinder;
	private final UserFinder userFinder;
	private final PasswordEncoder passwordEncoder;
	private final JwtValidator jwtValidator;


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// 일반 로그인
	@Transactional
	public UserLoginResponse login(UserLoginRequest request) {
		Optional<User> optionalUser = userRepository.findBySerialId(request.serialId());
		if (optionalUser.isEmpty()) {
			throw new BadRequestException(LoginErrorCode.USER_NOT_FOUND);
		}

		User user = optionalUser.get();

		// 토큰 생성
		Token issuedToken = jwtProvider.issueTokens(user.getId(), user.getUserRole().getValue());
		UserInfo userInfo = userInfoFinder.findByUserId(user.getId());
		userInfo.updateRefreshToken(issuedToken.refreshToken());
		return UserLoginResponse.of(issuedToken, true);
	}

	// 회원가입
	@Transactional
	public void join(UserJoinRequest request) {
		// 아이디 중복 확인
		Optional<User> existingUser = userRepository.findBySerialId(request.serialId());
		if (existingUser.isPresent()) {
			throw new BadRequestException(LoginErrorCode.ALREADY_EXISTED_ID);
		}

		// 비밀번호 일치 확인
		String encodedPassword = passwordEncoder.encode(request.password());
		if (!request.password().equals(request.repassword()) && !passwordEncoder.matches(request.password(), encodedPassword) ) {
			throw new BadRequestException(LoginErrorCode.PASSWORDS_DO_NOT_MATCH);
		}

		// 비밀번호 형식 검증
		if (!isValidPassword(request.password())) {
			throw new BadRequestException(LoginErrorCode.INVALID_PASSWORD_FORMAT);
		}

		// 이메일 형식 검증
        assert request.email() != null;
        if (!isValidEmail(request.email())) {
			throw new BadRequestException(LoginErrorCode.INVALID_EMAIL_FORMAT);
		}

		// 이메일 중복 확인
		Optional<UserInfo> existingUserInfo = userInfoRepository.findByEmail(request.email());
		if (existingUserInfo.isPresent()) {
			throw new BadRequestException(LoginErrorCode.ALREADY_EXISTED_EMAIL);
		}

		// 필수 정보 확인
		if (!isUserInfoValid(request)) {
			throw new BadRequestException(LoginErrorCode.MISSING_REQUIRED_FIELD);
		}

		User newUser = createUser(request.serialId(), Platform.SERVNOW);
		newUser.setPassword(request.password());

		userRepository.save(newUser);

		System.out.println(request.gender());
		Gender gender = Gender.getEnumGenderFromStringGender(request.gender());

		UserInfo newUserInfo = UserInfo.createMemberInfo(
				newUser, request.nickname(), gender, request.email(), request.birth(), null, "/roundLogo1.png");
		userInfoRepository.save(newUserInfo);
	}

	// 비밀번호 유효성 검증
	private boolean isValidPassword(String password) {
		String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,20}$";
		return password.matches(regex);
	}

	// 이메일 유효성 검증
	private boolean isValidEmail(String email) {
		String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		return email.matches(regex);
	}

	// 사용자 정보 유효성 검증
	private boolean isUserInfoValid(UserJoinRequest request) {
		return request.serialId() != null && !request.serialId().isEmpty() &&
			   request.password() != null && !request.password().isEmpty() &&
			   request.repassword() != null && !request.repassword().isEmpty() &&
			   request.nickname() != null && !request.nickname().isEmpty() &&
			   request.gender() != null && !request.gender().isEmpty() &&
			   request.email() != null && !request.email().isEmpty();
	}

	@Transactional(noRollbackFor = UnauthorizedException.class)
	public ReissueTokenResponse reissue(final String refreshToken) {

		long userId = jwtProvider.getSubject(refreshToken.substring(BEARER.length()));
		UserInfo userInfo = userInfoFinder.findByUserId(userId);

		validateRefreshToken(refreshToken, userInfo);

		Token reissuedToken = jwtProvider.issueTokens(userId, getUserRole(userId));
		userInfo.updateRefreshToken(reissuedToken.refreshToken());
		return ReissueTokenResponse.of(reissuedToken);
	}

	private String getUserRole(final long userId) {
		return userFinder.findById(userId).getUserRole().getValue();
	}

	private void validateRefreshToken(final String refreshToken, final UserInfo userInfo) {
		try {
			jwtValidator.validateRefreshToken(refreshToken);
			jwtValidator.equalsRefreshToken(refreshToken, userInfo.getRefreshToken());
		} catch (UnauthorizedException e) {
			logout(userInfo.getUser().getId());
			throw e;
		}
	}

	@Transactional
	public void logout(final long userId) {
		UserInfo findUserInfo = userInfoFinder.findByUserId(userId);
		findUserInfo.updateRefreshToken(null);
	}
}
