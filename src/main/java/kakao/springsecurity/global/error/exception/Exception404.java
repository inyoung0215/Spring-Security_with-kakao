package kakao.springsecurity.global.error.exception;

import kakao.springsecurity.global.util.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 찾을 수 없음
@Getter
public class Exception404 extends RuntimeException {
    public Exception404(String message) {
        super(message);
    }

    public ApiResponse.Result<?> body() {
        return ApiResponse.error(getMessage(), HttpStatus.NOT_FOUND);
    }

    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}