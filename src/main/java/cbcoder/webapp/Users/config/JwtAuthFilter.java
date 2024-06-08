package cbcoder.webapp.Users.config;

import cbcoder.webapp.Users.services.UserSecurityService;
import cbcoder.webapp.Users.services.impl.JwtServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtServiceImpl jwtService;
	private final UserSecurityService userSecurityService;

	public JwtAuthFilter(JwtServiceImpl jwtService, UserSecurityService userDetails) {
		this.jwtService = jwtService;
		this.userSecurityService = userDetails;
	}

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		final String jwt;
		final String userEmail;
		final String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		jwt = authorizationHeader.substring(7);
		userEmail = jwtService.getUsername(jwt);
		if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userSecurityService.userDetailsService().loadUserByUsername(userEmail);
			if (jwtService.validateToken(jwt, userDetails)) {
				SecurityContext security = SecurityContextHolder.createEmptyContext();
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				security.setAuthentication(authToken);
				SecurityContextHolder.setContext(security);
			}
		}
		filterChain.doFilter(request, response);
	}
}
