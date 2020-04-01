package com.caohao.filepan.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.caohao.filepan.dao.UserDao;
import com.caohao.filepan.entity.User;
import com.caohao.filepan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义的缓存工具
 * 由于用户表并不会经常的进行改动所有将用户信息存储到缓存中会极大的优化系统的访问速度
 */
@Component
public class MyCacheUtil {

    /**
     * 这边使用单例模式创建一个用户缓存表
     */
    private static ConcurrentHashMap<String, User> userCache;
    public static ConcurrentHashMap<String,User> GetUserCache(){
        if (userCache==null){
            userCache=new ConcurrentHashMap<>();
        }
        return userCache;
    }
    /**
     * 将所有的user用户信息存在缓存中
     */
    public static void updateUserCache(UserDao userDao){
        System.out.println(userDao);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",0);
        List<User> users = userDao.selectList(queryWrapper);
        GetUserCache();
        for (User user:users) {
            userCache.put(user.getUserName(),user);
        }
    }
    /**
     *通过username找到对应的user对象
     */
    public static   User getUsetByUserName(String userName){
        GetUserCache();
        if (userCache.get(userName)==null){return null;}
        return userCache.get(userName);
    }
}
