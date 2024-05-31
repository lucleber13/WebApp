package cbcoder.webapp.Users.services;

import cbcoder.webapp.Users.model.DTOs.JwtAuthResponse;
import cbcoder.webapp.Users.model.DTOs.RefreshTokenRequest;
import cbcoder.webapp.Users.model.DTOs.SignInRequest;
import cbcoder.webapp.Users.model.DTOs.SignUpRequest;
import cbcoder.webapp.Users.model.User;

public interface AuthService {
	User register(SignUpRequest request);
	JwtAuthResponse login(SignInRequest signInRequest);
	JwtAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
