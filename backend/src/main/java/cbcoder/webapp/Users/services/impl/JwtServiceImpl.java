package cbcoder.webapp.Users.services.impl;

import cbcoder.webapp.Users.services.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * This utility class simplifies the generation, extraction and validations of JWT tokens, Enhancing security and enabling stateless authentication mechanisms.
 *
 */
@Service
public class JwtServiceImpl implements JwtService { // implemented number 1.

	@Value("${webapp.security.jwt.secret}")
	private String secretKey;
	@Value("${webapp.security.jwt.expiration}")
	private long jwtExpiration;
	@Value("${webapp.security.jwt.expiration-refresh}")
	private long jwtExpirationRefresh;

	@Override
	public String generateJwtToken(UserDetails userDetails) {
		return Jwts.builder()
				.subject(userDetails.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + jwtExpiration))
				.signWith(getSigningKey())
				.compact();
	}

	private SecretKey getSigningKey() {
		byte[] keyBytes = secretKey.getBytes();
		return Keys.hmacShaKeyFor(keyBytes);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	@Override
	public String getUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	@Override
	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	@Override
	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	@Override
	public String generateRefreshJwtToken(Map<String, Object> claims, UserDetails userDetails) {
		return Jwts
				.builder()
				.claims(claims)
				.subject(userDetails.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + jwtExpirationRefresh))
				.signWith(getSigningKey())
				.compact();
	}
}
