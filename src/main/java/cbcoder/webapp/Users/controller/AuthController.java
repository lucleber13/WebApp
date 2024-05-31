package cbcoder.webapp.Users.controller;

import cbcoder.webapp.Users.model.DTOs.JwtAuthResponse;
import cbcoder.webapp.Users.model.DTOs.RefreshTokenRequest;
import cbcoder.webapp.Users.model.DTOs.SignInRequest;
import cbcoder.webapp.Users.model.DTOs.SignUpRequest;
import cbcoder.webapp.Users.model.User;
import cbcoder.webapp.Users.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody SignUpRequest request) {
		return ResponseEntity.ok(authService.register(request));
	}

	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> login(@RequestBody SignInRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@PostMapping("/refresh")
	public ResponseEntity<JwtAuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshToken) {
		return ResponseEntity.ok(authService.refreshToken(refreshToken));
	}

}
