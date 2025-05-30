package ru.jmdevelop.besshoptgbot.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import ru.jmdevelop.besshoptgbot.models.dom.ClientAction;
import ru.jmdevelop.besshoptgbot.models.dom.ClientOrder;
import ru.jmdevelop.besshoptgbot.models.dom.Command;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, ClientAction> clientActionRedisTemplate(RedisConnectionFactory factory) {
        return createRedisTemplate(factory, ClientAction.class);
    }

    @Bean
    public RedisTemplate<String, ClientOrder> clientOrderRedisTemplate(RedisConnectionFactory factory) {
        return createRedisTemplate(factory, ClientOrder.class);
    }

    @Bean
    public RedisTemplate<String, Command> commandRedisTemplate(RedisConnectionFactory factory) {
        return createRedisTemplate(factory, Command.class);
    }

    private <T> RedisTemplate<String, T> createRedisTemplate(
            RedisConnectionFactory factory,
            Class<T> type) {
        RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
