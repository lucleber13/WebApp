package cbcoder.webapp.Exceptions;

public class PasswordLengthNotValidException extends RuntimeException{
	public PasswordLengthNotValidException(String message) {
		super(message);
	}
}
