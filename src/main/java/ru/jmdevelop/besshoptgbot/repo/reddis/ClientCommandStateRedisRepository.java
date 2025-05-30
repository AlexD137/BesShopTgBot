package ru.jmdevelop.besshoptgbot.repo.redis;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.jmdevelop.besshoptgbot.models.dom.Command;
import ru.jmdevelop.besshoptgbot.repo.ClientCommandStateRepository;
import java.util.concurrent.TimeUnit;

@Repository
public class ClientCommandStateRedisRepository implements ClientCommandStateRepository {

    private final RedisTemplate<String, Command> redisTemplate;
    private final ListOperations<String, Command> listOps;
    private static final String KEY_PREFIX = "client:commands:";
    private static final long TTL_HOURS = 12;

    public ClientCommandStateRedisRepository(RedisTemplate<String, Command> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.listOps = redisTemplate.opsForList();
    }

    @Override
    public void pushByChatId(Long chatId, Command command) {
        String key = KEY_PREFIX + chatId;
        listOps.rightPush(key, command);
        redisTemplate.expire(key, TTL_HOURS, TimeUnit.HOURS);
    }

    @Override
    public Command popByChatId(Long chatId) {
        return listOps.leftPop(KEY_PREFIX + chatId);
    }

    @Override
    public void deleteAllByChatId(Long chatId) {
        redisTemplate.delete(KEY_PREFIX + chatId);
    }
}