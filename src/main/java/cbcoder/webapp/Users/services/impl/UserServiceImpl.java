package cbcoder.webapp.Users.services.impl;

import cbcoder.webapp.Users.repositories.UserRepository;
import cbcoder.webapp.Users.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	@Override
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
				return userRepository.findByEmail(email)
						.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
			}
		};
	}
}
