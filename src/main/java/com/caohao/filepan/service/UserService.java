package com.caohao.filepan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caohao.filepan.NotFoundException;
import com.caohao.filepan.dao.FileDao;
import com.caohao.filepan.dao.UserDao;
import com.caohao.filepan.entity.File;
import com.caohao.filepan.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service("userService")
public class UserService extends ServiceImpl<UserDao,User> {
    @Autowired
    UserDao userDao;
    @Autowired
    FileDao fileDao;
    /**
     * 用户注册
     */
    public User register(User user){
        //插入一个新的user数据但是他的个人文件夹id是null
        user.insert();
        //通过user的用户名唯一性找到新插入的user的id
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",user.getUserName());
        User selectOne = user.selectOne(queryWrapper);
        //新建一个个人文件夹
        File newFile = new File();
        //将userid插入到文件对象中
        newFile.setUserId(selectOne.getId());
        //设置这个用户的最外层文件夹url
        String src = selectOne.getUserName();
        newFile.setUrl(src);
        newFile.insert();
        //返回新插入的user对象
       return selectOne;
    }
    /**
     * 用户登录
     */
    public User login(String userName, String password, HttpSession session){
        //设置条件查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",userName).eq("password",password);
        //查出这个符合条件的user对象，如果不存在则报出未找到错误
        User user = userDao.selectOne(queryWrapper);
        if (user==null){
            return user;
        }else {
            //将用户对象存入session，并设置密码为***
            user.setPassword("******");
            session.setAttribute("user",user);
        }
        return user;
    }
    /**
     * 注销登录
     */
    public void logout(HttpSession session){
        if ( session.getAttribute("user")!=null){
            session.removeAttribute("user");
        }
    }
    /**
     * 用户删除
     */
    public boolean deleteUser(Integer id){
        Integer i = userDao.deleteById(id);
        if (i==null){
            return false;
        }else {
            return true;
        }
    }
    /**
     * 修改密码
     */
    /**
     * 修改邮箱
     */

}
