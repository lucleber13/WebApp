package cbcoder.webapp.Exceptions.handler;

import cbcoder.webapp.Exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionsHandler {
	private static final String MESSAGE = "message";

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult()
				.getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
		return errors;
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(UserNotFoundException.class)
	public Map<String, String> userNotFoundException(UserNotFoundException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put(MESSAGE, ex.getMessage());
		return errors;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UserAlreadyExistsException.class)
	public Map<String, String> userAlreadyExistsException(UserAlreadyExistsException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put(MESSAGE, ex.getMessage());
		return errors;
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(RoleNotFoundException.class)
	public Map<String, String> roleNotFoundException(RoleNotFoundException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put(MESSAGE, ex.getMessage());
		return errors;
	}

	@ResponseStatus(HttpStatus.LENGTH_REQUIRED)
	@ExceptionHandler(PasswordLengthNotValidException.class)
	public Map<String, String> passwordLengthNotValidException(PasswordLengthNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put(MESSAGE, ex.getMessage());
		return errors;
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(NotAuthorizedAccessException.class)
	public Map<String, String> notAuthorizedAccessException(NotAuthorizedAccessException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put(MESSAGE, ex.getMessage());
		return errors;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(EmailNotBindingException.class)
	public Map<String, String> emailNotBindingException(EmailNotBindingException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put(MESSAGE, ex.getMessage());
		return errors;
	}


}
