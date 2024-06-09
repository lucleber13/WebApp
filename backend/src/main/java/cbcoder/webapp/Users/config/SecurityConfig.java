package cbcoder.webapp.Users.config;

import cbcoder.webapp.Users.services.UserSecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
public class SecurityConfig {

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	private final JwtAuthFilter jwtAuthFilter;
	private final UserSecurityService userSecurityService;

	public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserSecurityService userSecurityService) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.userSecurityService = userSecurityService;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		logger.info("Security configuration initialized");
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(sessionManagement ->
						sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorizeRequests ->
						authorizeRequests
								.requestMatchers("auth/**").permitAll()
								.requestMatchers("users/**").hasAnyRole("SUPERADMIN", "ADMIN","SALES")
								.requestMatchers("superadmin/**").hasRole("SUPERADMIN")
								.anyRequest()
								.authenticated())
				.sessionManagement(sessionManagement ->
						sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userSecurityService.userDetailsService());
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

}
