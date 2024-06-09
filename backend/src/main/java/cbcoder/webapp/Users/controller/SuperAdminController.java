package cbcoder.webapp.Users.controller;

import cbcoder.webapp.Users.model.DTOs.UserDTO;
import cbcoder.webapp.Users.model.User;
import cbcoder.webapp.Users.services.impl.UserAdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("superadmin")
@PreAuthorize("hasAnyAuthority('ROLE_SUPERADMIN')")
//@CrossOrigin(origins = "http://localhost:4200")
public class SuperAdminController {

	private final UserAdminService userAdminService;

	public SuperAdminController(UserAdminService userAdminService) {
		this.userAdminService = userAdminService;
	}

	@PostMapping("/createAdmin")
	@PreAuthorize("hasAuthority('ROLE_SUPERADMIN')")
	public ResponseEntity<User> addAdminRole(@RequestBody @Valid UserDTO userDTO) {
		return ResponseEntity.ok(userAdminService.addAdminRole(userDTO));
	}

	@PutMapping("/revokeAdmin/{userId}")
	@PreAuthorize("hasAuthority('ROLE_SUPERADMIN')")
	public ResponseEntity<User> revokeAdmin(@PathVariable Long userId) {
		return ResponseEntity.ok(userAdminService.revokeAdmin(userId));
	}
}
