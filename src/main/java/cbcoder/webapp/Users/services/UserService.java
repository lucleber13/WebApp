package cbcoder.webapp.Users.services;

import cbcoder.webapp.Users.model.DTOs.UserDTO;
import cbcoder.webapp.Users.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {
	User updateUser(Long userId ,UserDTO userDTO);
	String deleteUser(Long userId);
	Page<User> getAllUsers(Pageable pageable);
	User getUserById(Long userId);

}
