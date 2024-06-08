package cbcoder.webapp.Users.services.impl;

import cbcoder.webapp.Users.model.DTOs.UserDTO;
import cbcoder.webapp.Users.model.User;

public interface UserAdminService {
	User addAdminRole(UserDTO userDTO);
	User revokeAdmin(Long userId);
}
