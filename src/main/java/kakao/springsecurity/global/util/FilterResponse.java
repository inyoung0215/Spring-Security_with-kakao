package kakao.springsecurity.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import kakao.springsecurity.global.error.exception.Exception400;
import kakao.springsecurity.global.error.exception.Exception401;
import kakao.springsecurity.global.error.exception.Exception403;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterResponse {

    private static final ObjectMapper OBJECT_MAPPER;
    private static final String CONTENT_TYPE;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        CONTENT_TYPE = "application/json; charset=utf-8";
    }

    public static void badRequest(HttpServletResponse response, Exception400 exception) throws IOException {
        response.setStatus(exception.status().value());
        response.setContentType(CONTENT_TYPE);

        String responseBody = OBJECT_MAPPER.writeValueAsString(exception.body());
        response.getWriter().println(responseBody);
    }

    public static void unAuthorized(HttpServletResponse response, Exception401 exception) throws IOException {
        response.setStatus(exception.status().value());
        response.setContentType(CONTENT_TYPE);

        String responseBody = OBJECT_MAPPER.writeValueAsString(exception.body());
        response.getWriter().println(responseBody);
    }

    public static void forbidden(HttpServletResponse response, Exception403 exception) throws IOException {
        response.setStatus(exception.status().value());
        response.setContentType(CONTENT_TYPE);

        String responseBody = OBJECT_MAPPER.writeValueAsString(exception.body());
        response.getWriter().println(responseBody);
    }
}