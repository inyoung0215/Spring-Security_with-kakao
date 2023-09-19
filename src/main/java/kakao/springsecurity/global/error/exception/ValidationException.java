package kakao.springsecurity.global.error.exception;

import kakao.springsecurity.global.util.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ValidationException extends RuntimeException {
	@Getter
	@AllArgsConstructor
	public static class ValidationError {
		private String field;
		private String message;
	}

	private List<ValidationError> validationErrors;

	public ValidationException(List<ValidationError> validationErrors) {
		this.validationErrors = validationErrors;
	}

	public ApiResponse.Result<?> body() {
		return ApiResponse.error(validationErrors, HttpStatus.BAD_REQUEST);
	}

	public HttpStatus status() {
		return HttpStatus.BAD_REQUEST;
	}
}
