package cbcoder.webapp.Users.services;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
	String generateJwtToken(UserDetails userDetails);
	String getUsername(String token);
	boolean isTokenExpired(String token);
	boolean validateToken(String token, UserDetails userDetails);
	String generateRefreshJwtToken(Map<String, Object> claims, UserDetails userDetails);
}
