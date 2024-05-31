package cbcoder.webapp.Users.services.impl;

import cbcoder.webapp.Exceptions.PasswordLengthNotValidException;
import cbcoder.webapp.Exceptions.UserAlreadyExistsException;
import cbcoder.webapp.Users.model.DTOs.*;
import cbcoder.webapp.Users.model.Role;
import cbcoder.webapp.Users.model.User;
import cbcoder.webapp.Users.model.enums.RoleEnum;
import cbcoder.webapp.Users.repositories.UserRepository;
import cbcoder.webapp.Users.services.AuthService;
import org.modelmapper.ModelMapper;
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
	private final ModelMapper modelMapper;

	public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtServiceImpl jwtService,
	                       AuthenticationManager authenticationManager, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
		this.modelMapper = modelMapper;
	}

	public User register(SignUpRequest request) {
		UserDTO userDTO = new UserDTO();
		userDTO.setFirstName(request.firstName());
		userDTO.setLastName(request.lastName());
		if (userRepository.existsByEmail(request.email())) {
			throw new UserAlreadyExistsException("User already exists with email " + request.email());
		}
		userDTO.setEmail(request.email());
		if (request.password().length() < 8) {
			throw new PasswordLengthNotValidException("Password should be at least 8 characters long");
		}
		userDTO.setPassword(passwordEncoder.encode(request.password()));
		userDTO.setEnabled(true);
		List<Role> roles = new ArrayList<>();
		if (request.roles() != null) {
			roles.addAll(request.roles());
		} else {
			roles.add(new Role(RoleEnum.ROLE_USER));
		}
		userDTO.setRoles(roles);
		User user = modelMapper.map(userDTO, User.class);
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
