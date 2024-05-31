package cbcoder.webapp.Users.services.impl;

import cbcoder.webapp.Users.model.DTOs.JwtAuthResponse;
import cbcoder.webapp.Users.model.DTOs.RefreshTokenRequest;
import cbcoder.webapp.Users.model.DTOs.SignInRequest;
import cbcoder.webapp.Users.model.DTOs.SignUpRequest;
import cbcoder.webapp.Users.model.Role;
import cbcoder.webapp.Users.model.User;
import cbcoder.webapp.Users.model.enums.RoleEnum;
import cbcoder.webapp.Users.repositories.UserRepository;
import cbcoder.webapp.Users.services.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtServiceImpl jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtServiceImpl jwtService, AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	public User register(SignUpRequest request) {
		User user = new User();
		user.setFirstName(request.firstName());
		user.setLastName(request.lastName());
		user.setEmail(request.email());
		user.setPassword(passwordEncoder.encode(request.password()));
		user.setEnabled(true);
		List<Role> roles = new ArrayList<>();
		if(request.roles() != null) {
			roles.addAll(request.roles());
		} else {
			roles.add(new Role(RoleEnum.ROLE_USER));
		}
		user.setRoles(roles);
		return userRepository.save(user);
	}

	public JwtAuthResponse login(SignInRequest signInRequest) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.email(), signInRequest.password()));
		var user = userRepository.findByEmail(signInRequest.email()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
		var jwt = jwtService.generateJwtToken(user);
		var refreshToken = jwtService.generateRefreshJwtToken(new HashMap<>(), user);

		return new JwtAuthResponse(jwt, refreshToken);

	}

	@Override
	public JwtAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
		String email = jwtService.getUsername(refreshTokenRequest.getRefreshToken());
		User user = userRepository.findByEmail(email).orElseThrow();
		if (jwtService.validateToken(refreshTokenRequest.getRefreshToken(), user)) {
			var jwt = jwtService.generateJwtToken(user);
			var newRefreshToken = jwtService.generateRefreshJwtToken(new HashMap<>(), user);
			return new JwtAuthResponse(jwt, newRefreshToken);
		} else {
			throw new IllegalArgumentException("Invalid refresh token");
		}
	}
}
