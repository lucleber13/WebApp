package cbcoder.webapp.Users.model.DTOs;

import cbcoder.webapp.Users.model.Role;

import java.util.List;

public record SignUpRequest(String firstName, String lastName, String email, String password, List<Role> roles) {
}
