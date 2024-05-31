package cbcoder.webapp.Users.model.DTOs;


import cbcoder.webapp.Users.model.Role;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class UserDTO {
	private Long userId;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private Boolean enabled;
	private LocalDateTime createdDate = LocalDateTime.now();
	private LocalDateTime updatedDate;
	private List<Role> roles;

	public UserDTO() {
	}

	public UserDTO(String firstName, String lastName, String email, String password, Boolean enabled, List<Role> roles) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.roles = roles;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public List<Role> getRoles() {
		return roles;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UserDTO userDTO)) return false;
		return Objects.equals(getFirstName(), userDTO.getFirstName());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getFirstName());
	}

	@Override
	public String toString() {
		return "UserDTO{" +
				"firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", active=" + enabled +
				", createdDate=" + createdDate +
				", updatedDate=" + updatedDate +
				", roles=" + roles +
				'}';
	}
}
