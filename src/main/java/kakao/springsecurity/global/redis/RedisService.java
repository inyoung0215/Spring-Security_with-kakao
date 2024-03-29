package kakao.springsecurity.global.redis;

import kakao.springsecurity.global.error.exception.Exception400;
import kakao.springsecurity.global.util.JsonConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisService {
    public static final String REFRESH_TOKEN_PREFIX = "RT ";
    public static final String LOGOUT_VALUE_PREFIX = "logout";

    private final RedisTemplate<String, String> redisTemplate;
    private final JsonConverter jsonConverter;

    public <T> T getObjectByKey(String key, Class<T> clazz) {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        String jsonString = vop.get(key);

        if (jsonString == null || jsonString.isEmpty()) {
            throw new Exception400("key", "만료 됐거나 유효 하지 않은 키 입니다.");
        }

        T object = jsonConverter.jsonToObject(jsonString, clazz);
        log.info("redis get 성공: " + object.toString());
        return object;
    }

    public void setObjectByKey(String key, Object obj, Long timeOut, TimeUnit timeUnit) {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        vop.set(key, jsonConverter.objectToJson(obj));
        redisTemplate.expire(key, timeOut, timeUnit);
        log.info("redis set 성공 key: " + key);
    }

    public void addBlacklist(String accessToken, String email, Long expiration) {
        // 리프레시 토큰 삭제
        deleteByKey(RedisService.REFRESH_TOKEN_PREFIX + email);
        // 엑세스 토큰은 만료 시점까지 블랙리스트 등록
        setObjectByKey(accessToken, RedisService.LOGOUT_VALUE_PREFIX, expiration, TimeUnit.MILLISECONDS);
    }

    public boolean deleteByKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
