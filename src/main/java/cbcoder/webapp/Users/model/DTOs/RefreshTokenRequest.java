package cbcoder.webapp.Users.model.DTOs;

public class RefreshTokenRequest {
	private String refreshToken;

	public RefreshTokenRequest() {
	}

	public RefreshTokenRequest(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

}
