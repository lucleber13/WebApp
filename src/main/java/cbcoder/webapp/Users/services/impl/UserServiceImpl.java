package cbcoder.webapp.Users.services.impl;

import cbcoder.webapp.Exceptions.UserAlreadyExistsException;
import cbcoder.webapp.Exceptions.UserNotFoundException;
import cbcoder.webapp.Users.model.DTOs.RoleDTO;
import cbcoder.webapp.Users.model.DTOs.UserDTO;
import cbcoder.webapp.Users.model.Role;
import cbcoder.webapp.Users.model.User;
import cbcoder.webapp.Users.repositories.RoleRepository;
import cbcoder.webapp.Users.repositories.UserRepository;
import cbcoder.webapp.Users.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
	private static final String USER_NOT_FOUND = "User not found with id ";
	private static final String USER_ALREADY_EXISTS = "User already exists with email ";
	private static final String ROLE_NOT_FOUND = "Role not found with id ";

	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final RoleRepository roleRepository;


	public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.roleRepository = roleRepository;
	}

	/**
	 * Save the user details in the database. The user details are saved based on the userDTO object passed as a parameter.
	 * The userDTO object contains the user details like firstName, lastName, email, password, and roles.
	 * The userDTO object is converted to a User object using the modelMapper and saved in the database.
	 * The saved User object is then converted back to a UserDTO object and returned.
	 * The support methods findUserByEmail and findRoles are used to find the user by email and roles respectively.
	 * @param userDTO The userDTO object containing the user details.
	 * @return UserDTO object containing the user details saved in the database.
	 */
	@Override
	public UserDTO saveUser(UserDTO userDTO) {
		findUserByEmail(userDTO.getEmail());
		User user = modelMapper.map(userDTO, User.class);
		findRoles(userDTO, user);
		User savedUser = userRepository.save(user);
		UserDTO savedUserDTO;
		savedUserDTO = modelMapper.map(savedUser, UserDTO.class);
		return savedUserDTO;
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
			user.setFirstName(userDTO.getFirstName());
			user.setLastName(userDTO.getLastName());
			if (!user.getEmail().equals(userDTO.getEmail())) {
				findUserByEmail(userDTO.getEmail());
			}
			user.setEmail(userDTO.getEmail());
			user.setPassword(userDTO.getPassword());
			user.setUpdatedDate(LocalDateTime.now());
			findRoles(userDTO, user);
			return userRepository.save(user);
		}
		throw new UserNotFoundException(USER_NOT_FOUND + userId);
	}

	/**
	 * Find the roles for the user based on the roleDTO object passed as a parameter.
	 * The roles are found based on the roleId in the roleDTO object.
	 * The roles are then added to the user object.
	 * @param userDTO The userDTO object containing the user details.
	 * @param user The user object to which the roles are to be added.
	 */
	private void findRoles(UserDTO userDTO, User user) {
		List<Role> roles = new ArrayList<>();
		if (userDTO.getRoles() != null) {
			for (RoleDTO roleDTO : userDTO.getRoles()) {
				Role role = roleRepository.findByRoleId(roleDTO.getRoleId())
						.orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND + roleDTO.getRoleId()));
				roles.add(role);
			}
		}
		user.setRoles(roles);
	}

	/**
	 * Find the user based on the email passed as a parameter.
	 * If the user is found, then throw a UserAlreadyExistsException.
	 * @param email The email of the user to be found.
	 */
	private void findUserByEmail(String email) {
		Optional<User> userOptional = userRepository.findByEmail(email);
		if (userOptional.isPresent()) {
			throw new UserAlreadyExistsException(USER_ALREADY_EXISTS + email);
		}
	}

	/**
	 * Get all users from the database with pagination support and sorting by a field in ascending order by default.
	 * @return Page<User> object containing the users in the database.
	 */
	@Override
	public Page<User> getAllUsers(Pageable pageable) {
		return userRepository.findAll(pageable);
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
