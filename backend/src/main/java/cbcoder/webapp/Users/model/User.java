package cbcoder.webapp.Users.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "USERS")
@SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
public class User implements Serializable, UserDetails {
	@Serial
	private static final long serialVersionUID = 4634563456378761L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
	@Column(nullable = false)
	private Long userId;

	@Column(name = "first_name", nullable = false)
	@NotNull(message = "First name cannot be null")
	@NotBlank(message = "First name cannot be blank")
	@Size(min = 2, max = 50, message = "First name should be between 2 and 50 characters")
	private String firstName;

	@Column(name = "last_name", nullable = false)
	@NotNull(message = "Last name cannot be null")
	@NotBlank(message = "Last name cannot be blank")
	@Size(min = 2, max = 50, message = "Last name should be between 2 and 50 characters")
	private String lastName;

	@Column(name = "email", unique = true, nullable = false)
	@Email(message = "Email should be valid")
	@NotNull(message = "Email cannot be null")
	@NotBlank(message = "Email cannot be blank")
	private String email;

	@Column(name = "password", nullable = false)
	@NotNull(message = "Password cannot be null")
	@NotBlank(message = "Password cannot be blank")
	@Size(min = 8, max = 100, message = "Password should be between 8 and 100 characters")
	private String password;

	@Column(name = "enabled")
	private Boolean enabled;

	@Column(name = "created_date", updatable = false)
	@CreatedDate
	private LocalDateTime createdDate = LocalDateTime.now();

	@Column(name = "updated_date")
	@LastModifiedDate
	private LocalDateTime updatedDate;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles = new ArrayList<>();

	public User() {
	}

	public User(Long userId, String firstName, String lastName, String email, String password, Boolean enabled, List<Role> roles) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
				.collect(Collectors.toList());
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName.toUpperCase().charAt(0) + firstName.substring(1).toLowerCase();
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		String[] names = lastName.split(" ");
		if (names.length > 1) {
			StringBuilder lastNameBuilder = new StringBuilder();
			for (String name : names) {
				lastNameBuilder.append(name.toUpperCase().charAt(0)).append(name.substring(1).toLowerCase()).append(" ");
			}
			this.lastName = lastNameBuilder.toString().trim();
			return;
		}
		this.lastName = lastName.toUpperCase().charAt(0) + lastName.substring(1).toLowerCase();
	}

	public void setEmail(@Email String email) {
		this.email = email;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	public List<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}

	public @Email String getEmail() {
		return email;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof User user)) return false;
		return Objects.equals(getUserId(), user.getUserId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getUserId());
	}

	@Override
	public String toString() {
		return "User{" +
				"userId=" + userId +
				", firstName='" + firstName + '\'' +
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
