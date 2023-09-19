package kakao.springsecurity.global.util;

import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse {
    public static <T> Result<T> error(T message, HttpStatus status) {
        return new Result<>(status.value(), false, null, new Error(message));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Result<T> {
        private final int status;
        private final boolean success;
        private final T response;
        private final Error error;
    }

    @Getter @Setter @AllArgsConstructor
    public static class Error<T> {
        private final T message;
    }
}
