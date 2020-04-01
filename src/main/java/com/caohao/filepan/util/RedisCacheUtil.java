package com.caohao.filepan.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.caohao.filepan.dao.UserDao;
import com.caohao.filepan.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义的缓存工具
 * 由于用户表并不会经常的进行改动所有将用户信息存储到缓存中会极大的优化系统的访问速度
 */
@Component
public class RedisCacheUtil {
//    @Autowired
//    UserDao userDao;
    @Resource
    RedisTemplate<String,Object> redisTemplate;


//    {
//        updateUserCache(userDao);
//    }
    /**
     * 将所有的user用户信息存在缓存中
     */
    public  void updateUserCache(UserDao userDao){
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
        System.out.println(userDao);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",0);
        List<User> users = userDao.selectList(queryWrapper);
        for (User u :users) {
            opsForValue.set(u.getUserName(),u);
        }

    }
    /**
     *
     */
    public void setNewUser(User user){
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
        opsForValue.set(user.getUserName(),user);
    }
    /**
     *
     */
    public void deleteUser(String userName){
        redisTemplate.delete(userName);
    }
    /**
     *通过username找到对应的user对象
     */
    public  User getUsetByUserName(String userName) {
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
        User user = (User) opsForValue.get(userName);
        return user;
    }
}
