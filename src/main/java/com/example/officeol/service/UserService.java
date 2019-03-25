/*
package com.example.officeol.service;

import com.example.officeol.bean.User;
import com.example.officeol.bean.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RedisTemplate<String, User> redisTemplate;


    public void createUser(User user) {
        logger.info("创建用户start...");
        userList.add(user);
    }

    public User getById(String id) {
        logger.info("获取用户start...");
        // 从缓存中获取用户信息
        String key = id;
        ValueOperations<String, User> operations = redisTemplate.opsForValue();

        // 缓存存在
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            User user = operations.get(key);
            logger.info("从缓存中获取了用户 id = " + id);
            return user;
        }

        // 缓存不存在，从 DB 中获取
       // User user = userMapper.selectById(id);
        // 插入缓存
        //operations.set(key, user, 10, TimeUnit.SECONDS);
        return null;
    }

*/
/*    public void updateUser(User user) {
        logger.info("更新用户start...");
        userMapper.updateById(user);
        int userId = user.getId();
        // 缓存存在，删除缓存
        String key = "user_" + userId;
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);
            logger.info("更新用户时候，从缓存中删除用户 >> " + userId);
        }
    }*//*


*/
/*    public void deleteById(int id) {
        logger.info("删除用户start...");
        userMapper.deleteById(id);

        // 缓存存在，删除缓存
        String key = "user_" + id;
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);
            logger.info("更新用户时候，从缓存中删除用户 >> " + id);
        }
    }*//*


}
*/
