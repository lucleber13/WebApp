package cbcoder.webapp.Users.controller;

import cbcoder.webapp.Users.model.DTOs.UserDTO;
import cbcoder.webapp.Users.model.User;
import cbcoder.webapp.Users.services.UserServiceReserve;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

	private final UserServiceReserve userServiceReserve;

	public UserController(UserServiceReserve userServiceReserve) {
		this.userServiceReserve = userServiceReserve;
	}

	/**
	 * Get all users with pagination and sorting options available as query parameters.
	 * The default values are pageNo=0, pageSize=10, sortBy=userId, which can be overridden by passing the values as query parameters.
	 * @param pageNo Integer pageNo (default value is 0) for pagination of users list to be fetched.
	 * @param pageSize Integer pageSize (default value is 10) for pagination of users list to be fetched.
	 * @param sortBy String sortBy (default value is userId) for sorting of users list to be fetched.
	 * @return ResponseEntity<Page<User>> Page of users list with pagination and sorting options.
	 */
	@GetMapping("/all")
	@PreAuthorize("hasAnyAuthority('ROLE_SUPERADMIN','ROLE_ADMIN', 'ROLE_SALES')")
	public ResponseEntity<Page<User>> getAllUsers(
			@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "userId") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		return ResponseEntity.ok(userServiceReserve.getAllUsers(pageable));
	}

	/**
	 * Get the user details based on the userId.
	 * @param userId The userId of the user to be fetched.
	 * @return ResponseEntity<User> The user object containing the user details fetched from the database.
	 */
	@PutMapping("/{userId}")
	@PreAuthorize("hasAnyAuthority('ROLE_SUPERADMIN','ROLE_ADMIN', 'ROLE_SALES')")
	public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
		User user = userServiceReserve.updateUser(userId, userDTO);
		return ResponseEntity.ok(user);
	}

	/**
	 * Get the user details based on the userId.
	 * @param userId The userId of the user to be fetched.
	 * @return ResponseEntity<User> The user object containing the user details fetched from the database.
	 */
	@GetMapping("/{userId}")
	@PreAuthorize("hasAnyAuthority('ROLE_SUPERADMIN','ROLE_ADMIN', 'ROLE_SALES')")
	public ResponseEntity<User> getUserById(@PathVariable Long userId) {
		return ResponseEntity.ok(userServiceReserve.getUserById(userId));
	}

	/**
	 * Delete the user based on the userId.
	 * @param userId The userId of the user to be deleted.
	 * @return ResponseEntity<Void> The response entity with status 200 OK.
	 */
	@DeleteMapping("/{userId}")
	@PreAuthorize("hasAnyAuthority('ROLE_SUPERADMIN','ROLE_ADMIN', 'ROLE_SALES')")
	public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
		return ResponseEntity.ok(userServiceReserve.deleteUser(userId));
	}
}
