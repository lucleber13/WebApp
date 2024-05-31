package cbcoder.webapp.Exceptions;

public class NotAuthorizedAccessException extends RuntimeException{
	public NotAuthorizedAccessException(String message) {
		super(message);
	}
}
