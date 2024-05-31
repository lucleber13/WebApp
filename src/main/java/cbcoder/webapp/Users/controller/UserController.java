package cbcoder.webapp.Users.controller;

import cbcoder.webapp.Users.model.DTOs.UserDTO;
import cbcoder.webapp.Users.model.User;
import cbcoder.webapp.Users.services.UserServiceReserve;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

	private final UserServiceReserve userServiceReserve;

	public UserController(UserServiceReserve userServiceReserve) {
		this.userServiceReserve = userServiceReserve;
	}

	/**
	 * Register a new user with the details provided in the userDTO object.
	 * The userDTO object contains the user details like firstName, lastName, email, password, and roles.
	 * The URI is created using the ServletUriComponentsBuilder and the user details are saved in the database.
	 * Then the HTTP Status 201 Created is returned with the userDTO object containing the user details saved in the database.
	 * @param userDTO The userDTO object containing the user details.
	 * @return ResponseEntity<UserDTO> The userDTO object containing the user details saved in the database.
	 */
	@PostMapping("/register")
	public ResponseEntity<UserDTO> registerUser(@RequestBody @Valid UserDTO userDTO) {
		URI uri = URI.create(
				ServletUriComponentsBuilder
						.fromCurrentContextPath()
						.path("/users/register")
						.toUriString());
		return ResponseEntity.created(uri).body(userServiceReserve.saveUser(userDTO));
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
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<User> getUserById(@PathVariable Long userId) {
		return ResponseEntity.ok(userServiceReserve.getUserById(userId));
	}

	/**
	 * Delete the user based on the userId.
	 * @param userId The userId of the user to be deleted.
	 * @return ResponseEntity<Void> The response entity with status 200 OK.
	 */
	@DeleteMapping("/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
		return ResponseEntity.ok(userServiceReserve.deleteUser(userId));
	}
}
