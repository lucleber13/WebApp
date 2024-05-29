package cbcoder.webapp.Users.services;

import cbcoder.webapp.Users.model.DTOs.UserDTO;
import cbcoder.webapp.Users.model.User;

import java.util.List;

public interface UserService {
	UserDTO saveUser(UserDTO userDTO);
	User getUser(Long userId);
	User updateUser(UserDTO userDTO);
	void deleteUser(Long userId);
	List<User> getAllUsers();
}
