package cbcoder.webapp.Users.model;

import cbcoder.webapp.Users.model.enums.RoleEnum;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "ROLES")
@SequenceGenerator(name = "role_seq", sequenceName = "role_seq", allocationSize = 1)
public class Role implements Serializable {

	@Serial
	private static final long serialVersionUID = 4634563456378761L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
	private Long roleId;

	@Enumerated(EnumType.STRING)
	@Column(name = "role_name")
	private RoleEnum roleName;


	public Role() {
	}

	public Role(RoleEnum roleName) {
		this.roleName = roleName;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public RoleEnum getRoleName() {
		return roleName;
	}

	public void setRoleName(RoleEnum roleName) {
		this.roleName = roleName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Role role)) return false;
		return Objects.equals(getRoleId(), role.getRoleId()) && getRoleName() == role.getRoleName();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getRoleId(), getRoleName());
	}

	@Override
	public String toString() {
		return "Role{" +
				"roleId=" + roleId +
				", roleName=" + roleName +
				'}';
	}
}