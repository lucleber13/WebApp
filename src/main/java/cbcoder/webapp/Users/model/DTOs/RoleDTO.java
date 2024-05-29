package cbcoder.webapp.Users.model.DTOs;

public class RoleDTO {
	private Long roleId;
	private String roleName;

	public RoleDTO() {
	}

	public RoleDTO(Long roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
	}

	public Long getRoleId() {
		return roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	@Override
	public String toString() {
		return "RoleDTO{" +
				"roleId=" + roleId +
				", roleName='" + roleName + '\'' +
				'}';
	}
}
