package org.example.service.impl;

import org.example.domain.UserContext;
import org.example.domain.UserContextMessage;
import org.example.service.UserContextService;
import org.example.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserContextServiceImpl implements UserContextService {

    private static final Integer MAX_LEN = 2;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public UserContext getUserContext(String userId) {
        String key = "userContext:" + userId;
        Long size = redisTemplate.opsForList().size(key);
        if (size == null || size == 0) {
            return null;
        } else if (size > MAX_LEN) {
            redisTemplate.opsForList().rightPop(key);
        }
        List<UserContextMessage> range = redisTemplate.opsForList().range(key, 0, -1);
        UserContext userContext = new UserContext();
        userContext.setUserMessage(range);
        userContext.setUserId(userId);
        return userContext;
    }

    @Override
    public void addUserContextMessage(String userId, UserContextMessage userContextMessage) {
        String key = "userContext:" + userId;
        redisTemplate.opsForList().leftPush(key, userContextMessage);
        Long size = redisTemplate.opsForList().size(key);
        if (size > MAX_LEN) {
            redisTemplate.opsForList().rightPop(key);
        }
    }


}
