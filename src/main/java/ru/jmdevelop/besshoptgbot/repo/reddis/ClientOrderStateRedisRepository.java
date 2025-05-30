package ru.jmdevelop.besshoptgbot.repo.reddis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.jmdevelop.besshoptgbot.models.dom.ClientOrder;
import ru.jmdevelop.besshoptgbot.repo.ClientOrderStateRepository;

import java.util.concurrent.TimeUnit;

@Repository
public class ClientOrderStateRedisRepository implements ClientOrderStateRepository {

    private final RedisTemplate<String, ClientOrder> redisTemplate;
    private static final String KEY_PREFIX = "client:order:";
    private static final long TTL_HOURS = 48;

    public ClientOrderStateRedisRepository(RedisTemplate<String, ClientOrder> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ClientOrder findByChatId(Long chatId) {
        return redisTemplate.opsForValue().get(KEY_PREFIX + chatId);
    }

    @Override
    public void updateByChatId(Long chatId, ClientOrder order) {
        redisTemplate.opsForValue().set(
                KEY_PREFIX + chatId,
                order,
                TTL_HOURS,
                TimeUnit.HOURS
        );
    }

    @Override
    public void deleteByChatId(Long chatId) {
        redisTemplate.delete(KEY_PREFIX + chatId);
    }
}