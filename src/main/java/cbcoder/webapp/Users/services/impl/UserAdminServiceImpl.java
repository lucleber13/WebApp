package cbcoder.webapp.Users.services.impl;

import cbcoder.webapp.Exceptions.*;
import cbcoder.webapp.Users.model.DTOs.UserDTO;
import cbcoder.webapp.Users.model.Role;
import cbcoder.webapp.Users.model.User;
import cbcoder.webapp.Users.model.enums.RoleEnum;
import cbcoder.webapp.Users.repositories.RoleRepository;
import cbcoder.webapp.Users.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserAdminServiceImpl implements UserAdminService{
	private static final String ROLE_NOT_FOUND = "Role not found with id ";

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final ModelMapper modelMapper;

	public UserAdminServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.modelMapper = modelMapper;
	}



	/**
	 * Create an Admin in the database. The Admin details are passed as a parameter in the userDTO object.
	 * This method will check if the app have some user already as an admin, if not, it will add admin role to user.
	 * To create and admin, only the superadmin can do it. If the user is not a superadmin, it will throw a NotAuthorizedAccessException.
	 * The user details are then mapped to the user object and saved in the database.
	 * @param userDTO The userDTO object containing the user details.
	 * @return User object containing the user details saved in the database.
	 */
	@Override
	public User addAdminRole(UserDTO userDTO) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals(RoleEnum.ROLE_SUPERADMIN.name()))) {
			throw new NotAuthorizedAccessException("Only superadmin can create an admin");
		}
		User user = getUserById(userDTO.getUserId());
		if (!user.getEmail().equals(userDTO.getEmail())) {
			throw new EmailNotBindingException("Email not matching with the user email");
		}

		// Check if the user already has the admin role
		Role adminRole = roleRepository.findByRoleName(RoleEnum.ROLE_ADMIN)
				.orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND + RoleEnum.ROLE_ADMIN.name()));

		if (user.getRoles().contains(adminRole)) {
			throw new UserAlreadyExistsException("User already has the admin role");
		} else {
			user.getRoles().add(adminRole);
			user.setUpdatedDate(LocalDateTime.now());
			return userRepository.save(user);
		}

	}

	/**
	 * Revoke the admin role from the user based on the userId passed as a parameter.
	 * The user is found based on the userId and the admin role is revoked from the user.
	 * Only the superadmin can revoke the admin role. If the user is not a superadmin, it will throw a NotAuthorizedAccessException.
	 * @param userId The userId of the user to revoke the admin role.
	 * @return User object containing the user details with the admin role revoked.
	 */
	@Override
	public User revokeAdmin(Long userId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals(RoleEnum.ROLE_SUPERADMIN.name()))) {
			throw new NotAuthorizedAccessException("Only superadmin can revoke an admin");
		}
		User user = getUserById(userId);

		// Check if the user already has the admin role
		Role adminRole = roleRepository.findByRoleName(RoleEnum.ROLE_ADMIN)
				.orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND + RoleEnum.ROLE_ADMIN.name()));

		if (user.getRoles().contains(adminRole)) {
			user.getRoles().remove(adminRole);
			return userRepository.save(user);
		} else {
			throw new RoleNotFoundException("User does not have the admin role");
		}
	}

	private User getUserById(Long userId) {
		Optional<User> userOptional = userRepository.findById(userId);
		if (userOptional.isPresent()) {
			return userOptional.get();
		}
		throw new UserNotFoundException("User not found with id " + userId);
	}

}
