package cbcoder.webapp.Users.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "USERS")
@SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
public class User implements Serializable {
	@Serial
	private static final long serialVersionUID = 4634563456378761L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
	private Long userId;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "email", unique = true)
	@Email
	private String email;
	@Column(name = "password")
	private String password;
	@Column(name = "active")
	private Boolean active;
	@Column(name = "created_date")
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

	public User(String firstName, String lastName, String email, String password, Boolean active, List<Role> roles) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.active = active;
		this.roles = roles;
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
		this.lastName = lastName.toUpperCase().charAt(0) + lastName.substring(1).toLowerCase();
	}

	public @Email String getEmail() {
		return email;
	}

	public void setEmail(@Email String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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

	public List<Role> getRoles(Role roles) {
		return this.roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(getUserId(), user.getUserId())
				&& Objects.equals(getFirstName(), user.getFirstName())
				&& Objects.equals(getLastName(), user.getLastName())
				&& Objects.equals(getEmail(), user.getEmail())
				&& Objects.equals(getPassword(), user.getPassword())
				&& Objects.equals(getActive(), user.getActive())
				&& Objects.equals(getCreatedDate(), user.getCreatedDate())
				&& Objects.equals(getUpdatedDate(), user.getUpdatedDate())
				&& Objects.equals(roles, user.roles);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUserId(), getFirstName(), getLastName(), getEmail(), getPassword(), getActive(), getCreatedDate(), getUpdatedDate(), roles);
	}

	@Override
	public String toString() {
		return "User{" +
				"userId=" + userId +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", active=" + active +
				", createdDate=" + createdDate +
				", updatedDate=" + updatedDate +
				", roles=" + roles +
				'}';
	}
}
