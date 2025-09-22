package uz.oson.taxi.service;

import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import uz.oson.taxi.entity.UserState;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserStateCacheService {

    private final RedisTemplate<String, UserState> userRedisTemplate;
    @Value("${spring.data.redis.user-prefix}")
    private String keyPrefix;

    @Value("${spring.data.redis.ttl-hours}")
    private long ttlHours;

    // Получить UserState из кэша
    public UserState get(Long chatId) {
        return userRedisTemplate.opsForValue().get(keyPrefix + chatId);
    }

    // Сохранить UserState в кэш
    public void put(UserState userState) {
        userRedisTemplate.opsForValue().set(keyPrefix + userState.getChatId(), userState, Duration.ofHours(ttlHours));
    }

    // Удалить UserState из кэша
    public void evict(Long chatId) {
        userRedisTemplate.delete(keyPrefix + chatId);
    }

    public boolean exists(Long chatId) {
        return Boolean.TRUE.equals(userRedisTemplate.hasKey(keyPrefix + chatId));
    }

}
