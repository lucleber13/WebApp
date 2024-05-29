package cbcoder.webapp.Users.controller;

import cbcoder.webapp.Users.model.DTOs.UserDTO;
import cbcoder.webapp.Users.model.User;
import cbcoder.webapp.Users.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<UserDTO> registerUser(@RequestBody @Valid UserDTO userDTO) {
		UserDTO user = userService.saveUser(userDTO);
		return ResponseEntity.ok(user);
	}

	@GetMapping("/all")
	public ResponseEntity<Iterable<User>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

}
