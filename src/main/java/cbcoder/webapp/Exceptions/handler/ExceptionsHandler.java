package cbcoder.webapp.Exceptions.handler;

import cbcoder.webapp.Exceptions.RoleNotFoundException;
import cbcoder.webapp.Exceptions.UserAlreadyExistsException;
import cbcoder.webapp.Exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionsHandler {
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
		errors.put("message", ex.getMessage());
		return errors;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UserAlreadyExistsException.class)
	public Map<String, String> userAlreadyExistsException(UserAlreadyExistsException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put("message", ex.getMessage());
		return errors;
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(RoleNotFoundException.class)
	public Map<String, String> roleNotFoundException(RoleNotFoundException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put("message", ex.getMessage());
		return errors;
	}

}
