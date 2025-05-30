package ru.jmdevelop.besshoptgbot.repo.reddis;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.jmdevelop.besshoptgbot.models.dom.ClientAction;
import ru.jmdevelop.besshoptgbot.repo.ClientActionRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Repository
public class ClientActionRedisRepository implements ClientActionRepository {

    private final RedisTemplate<String, ClientAction> redisTemplate;
    private static final String KEY_PREFIX = "client:action:";
    private static final long TTL_HOURS = 24;

    public ClientActionRedisRepository(RedisTemplate<String, ClientAction> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ClientAction findByChatId(Long chatId) {
        return redisTemplate.opsForValue().get(KEY_PREFIX + chatId);
    }

    @Override
    public void updateByChatId(Long chatId, ClientAction action) {
        redisTemplate.opsForValue().set(
                KEY_PREFIX + chatId,
                action,
                TTL_HOURS,
                TimeUnit.HOURS
        );
    }

    @Override
    public void deleteByChatId(Long chatId) {
        redisTemplate.delete(KEY_PREFIX + chatId);
    }
}
