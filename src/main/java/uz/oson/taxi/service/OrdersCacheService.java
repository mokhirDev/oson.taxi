package uz.oson.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import uz.oson.taxi.entity.Orders;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class OrdersCacheService {

    private final RedisTemplate<String, Orders> orderRedisTemplate;
    @Value("${spring.data.redis.order-prefix}")
    private String keyPrefix;

    @Value("${spring.data.redis.ttl-hours}")
    private long ttlHours;

    public Orders get(Long chatId) {
        return orderRedisTemplate.opsForValue().get(keyPrefix + chatId);
    }

    public void put(Orders order) {
        orderRedisTemplate.opsForValue().set(keyPrefix + order.getChatId(), order, Duration.ofHours(ttlHours));
    }

    public void evict(Long chatId) {
        orderRedisTemplate.delete(keyPrefix + chatId);
    }

    public boolean exists(Long orderId) {
        return Boolean.TRUE.equals(orderRedisTemplate.hasKey(keyPrefix + orderId));
    }

}
