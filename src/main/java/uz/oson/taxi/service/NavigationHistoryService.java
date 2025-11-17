package uz.oson.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import uz.oson.taxi.entity.enums.CityEnum;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class NavigationHistoryService {
    @Value("${spring.data.redis.last-message-id-prefix}")
    private String lastMessageId;

    @Value("${spring.data.redis.page-history-prefix}")
    private String pageHistory;

    @Value("${spring.data.redis.driver-route-prefix}")
    private String driverRoute;

    private final RedisTemplate<String, String> pageHistoryRedisTemplate;
    private final RedisTemplate<String, Integer> lastMessageRedisTemplate;
    private final RedisTemplate<String, String> driverRoutesRedisTemplate;

    private String pageHistoryKey(Long chatId) {
        return pageHistory + chatId;
    }

    private String lastMessageIdKey(Long chatId) {
        return lastMessageId + chatId;
    }

    private String driverRouteKey(Long chatId) {
        return driverRoute + chatId;
    }

    // PAGE history (stack)
    public void push(Long chatId, String pageId) {
        pageHistoryRedisTemplate.opsForList().rightPush(pageHistoryKey(chatId), pageId);
        pageHistoryRedisTemplate.expire(pageHistoryKey(chatId), Duration.ofHours(24));
    }

    public void clear(Long chatId) {
        pageHistoryRedisTemplate.delete(pageHistoryKey(chatId));
    }

    public boolean isExistPageHistory(Long chatId) {
        return pageHistoryRedisTemplate.hasKey(pageHistoryKey(chatId));
    }

    public String current(Long chatId) {
        return pageHistoryRedisTemplate.opsForList().index(pageHistoryKey(chatId), -1);
    }

    public void rightPop(Long chatId) {
        // удаляем текущую
        pageHistoryRedisTemplate.opsForList().rightPop(pageHistoryKey(chatId));
    }

    // MESSAGE ids operations - store small list of Integer
    public void pushMessageId(Long chatId, Integer messageId, int maxDepth) {
        String key = lastMessageIdKey(chatId);
        lastMessageRedisTemplate.opsForList().rightPush(key, messageId);
        lastMessageRedisTemplate.expire(key, Duration.ofHours(24));
        // trim to keep only last maxDepth elements
        lastMessageRedisTemplate.opsForList().trim(key, -maxDepth, -1);
    }

    public Integer peekLastMessage(Long chatId) {
        return lastMessageRedisTemplate.opsForList().index(lastMessageIdKey(chatId), -1);
    }

    public List<Integer> consumeAll(Long chatId) {
        String key = lastMessageIdKey(chatId);
        List<Integer> all = lastMessageRedisTemplate.opsForList().range(key, 0, -1);
        if (all == null || all.isEmpty()) return Collections.emptyList();
        // copy all except last
        List<Integer> toDelete = new ArrayList<>(all.subList(0, all.size()));
        // now trim redis list to keep only last element (index -1)
        lastMessageRedisTemplate.opsForList().trim(key, 1, 0);
        return toDelete;
    }

    public void deleteAllMessageIds(Long chatId) {
        lastMessageRedisTemplate.delete(lastMessageIdKey(chatId));
    }

    public List<Integer> getAllMessageIds(Long chatId) {
        return lastMessageRedisTemplate.opsForList().range(lastMessageIdKey(chatId), 0, -1);
    }

    public void pushDriverRoute(Long chatId, CityEnum city) {
        String key = driverRouteKey(chatId);
        driverRoutesRedisTemplate.opsForList().rightPush(key, city.name());
        driverRoutesRedisTemplate.expire(key, Duration.ofHours(24));
    }
}