package cbcoder.webapp.Users.services.impl;

import cbcoder.webapp.Exceptions.EmailNotBindingException;
import cbcoder.webapp.Exceptions.UserNotFoundException;
import cbcoder.webapp.Users.model.DTOs.UserDTO;
import cbcoder.webapp.Users.model.User;
import cbcoder.webapp.Users.repositories.UserRepository;
import cbcoder.webapp.Users.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
	private static final String USER_NOT_FOUND = "User not found with id ";

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;


	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}


	/**
	 * Update the user details in the database. The user details are updated based on the userId.
	 * The user details are updated based on the userDTO object passed as a parameter.
	 * The userDTO object contains the updated user details.
	 * @param userId The userId of the user to be updated.
	 * @param userDTO The userDTO object containing the updated user details.
	 * @return User object containing the updated user details.
	 */
	@Override
	public User updateUser(Long userId, UserDTO userDTO) {
		Optional<User> userOptional = userRepository.findById(userId);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			if (userDTO.getEmail().equals(user.getEmail())) {
				user.setFirstName(userDTO.getFirstName());
				user.setLastName(userDTO.getLastName());
				user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
				user.setUpdatedDate(LocalDateTime.now());
				return userRepository.save(user);
			} else {
				throw new EmailNotBindingException("Email not matching with the user email");
			}
		} else {
			throw new UserNotFoundException(USER_NOT_FOUND + userId);
		}
	}

	/**
	 * Get all users from the database with pagination support and sorting by a field in ascending order by default.
	 * @return Page<User> object containing the users in the database.
	 */
	@Override
	public Page<User> getAllUsers(Pageable pageable) {
		Page<User> users = userRepository.findAll(pageable);
		if(users.isEmpty()) {
			throw new UserNotFoundException("No users found in the database");
		}
		return users;
	}

	/**
	 * Get the user based on the userId passed as a parameter.
	 * @param userId The userId of the user to be fetched.
	 * @return User object containing the user details fetched from the database.
	 */
	@Override
	public User getUserById(Long userId) {
		Optional<User> userOptional = userRepository.findById(userId);
		if (userOptional.isPresent()) {
			return userOptional.get();
		}
		throw new UserNotFoundException(USER_NOT_FOUND + userId);
	}

	/**
	 * Delete the user based on the userId passed as a parameter.
	 * @param userId The userId of the user to be deleted.
	 */
	@Override
	public String deleteUser(Long userId) {
		Optional<User> userOptional = userRepository.findById(userId);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			userRepository.delete(user);
			return "User with name " + user.getFullName() + " deleted successfully";
		}
		throw new UserNotFoundException(USER_NOT_FOUND + userId);
	}

}
