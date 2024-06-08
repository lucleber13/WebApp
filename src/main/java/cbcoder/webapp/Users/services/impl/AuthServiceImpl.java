package cbcoder.webapp.Users.services.impl;

import cbcoder.webapp.Exceptions.PasswordLengthNotValidException;
import cbcoder.webapp.Exceptions.RoleNotFoundException;
import cbcoder.webapp.Exceptions.UserAlreadyExistsException;
import cbcoder.webapp.Users.model.DTOs.*;
import cbcoder.webapp.Users.model.Role;
import cbcoder.webapp.Users.model.User;
import cbcoder.webapp.Users.repositories.RoleRepository;
import cbcoder.webapp.Users.repositories.UserRepository;
import cbcoder.webapp.Users.services.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Service
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtServiceImpl jwtService;
	private final AuthenticationManager authenticationManager;
	private final ModelMapper modelMapper;
	private final RoleRepository roleRepository;

	public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtServiceImpl jwtService,
	                       AuthenticationManager authenticationManager, ModelMapper modelMapper, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
		this.modelMapper = modelMapper;
		this.roleRepository = roleRepository;
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
		Optional<Role> role = roleRepository.findByRoleId(request.roles().getFirst().getRoleId());
		if (role.isPresent()) {
			userDTO.setRoles(List.of(role.get()));
		} else {
			throw new RoleNotFoundException("Role not found");
		}
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
