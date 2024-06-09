package cbcoder.webapp.Users.controller;

import cbcoder.webapp.Users.model.DTOs.JwtAuthResponse;
import cbcoder.webapp.Users.model.DTOs.RefreshTokenRequest;
import cbcoder.webapp.Users.model.DTOs.SignInRequest;
import cbcoder.webapp.Users.model.DTOs.SignUpRequest;
import cbcoder.webapp.Users.model.User;
import cbcoder.webapp.Users.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody @Valid SignUpRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder
						.fromCurrentContextPath()
						.path("/api/v1/auth/register")
						.toUriString());
		return ResponseEntity.created(uri).body(authService.register(request));
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
