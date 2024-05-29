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
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final RoleRepository roleRepository;

	public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.roleRepository = roleRepository;
	}

	@Override
	public UserDTO saveUser(UserDTO userDTO) {
		// Check if the user already exists by email
		Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());
		if (userOptional.isPresent()) {
			throw new UserAlreadyExistsException("User already exists");
		}

		// Map UserDTO to User entity
		User user = modelMapper.map(userDTO, User.class);

		// Map roles from RoleDTO to Role entities
		List<Role> roles = new ArrayList<>();
		if (userDTO.getRoles() != null) {
			for (RoleDTO roleDTO : userDTO.getRoles()) {
				Role role = roleRepository.findByRoleId(roleDTO.getRoleId())
						.orElseThrow(() -> new RuntimeException("Role not found"));
				roles.add(role);
			}
		}
		user.setRoles(roles);

		// Debug statement to check roles before saving
		System.out.println("Roles before saving: " + user);

		// Save user entity to the database
		User savedUser = userRepository.save(user);

		// Debug statement to check saved user and roles
		System.out.println("Saved User with Roles: " + savedUser);

		// Map saved User entity back to UserDTO
		UserDTO savedUserDTO = modelMapper.map(savedUser, UserDTO.class);

		// Debug statement to check final DTO roles
		System.out.println("Final UserDTO with Roles: " + savedUserDTO.getRoles());

		return savedUserDTO;
	}

	@Override
	public User getUser(Long userId) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()) {
			return user.get();
		}
		throw new UserNotFoundException("User not found");
	}

	@Override
	public User updateUser(UserDTO userDTO) {
		return null;
	}

	@Override
	public void deleteUser(Long userId) {

	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = userRepository.findAll();
		if (users.isEmpty()) {
			throw new UserNotFoundException("No users found");
		}
		return users;
	}
}
