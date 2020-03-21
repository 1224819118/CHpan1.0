package com.caohao.filepan.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("tb_user")
public class User extends Model<User> {
    //设置主键我使用的是包装类，包装类可以为空，所以可以减少应为基本类型默认值的问题
    @TableId(type=IdType.AUTO)
    private Integer id;
    //用户名user_name
    private String userName;
    //昵称nick_name
    private String nickName;
    //密码password
    private String password;
    //邮箱emial
    private String emial;
    //存在状态,用于逻辑删除
    @TableLogic(value = "0",delval = "1")
    private int status;
    /**
     * 这个方法是继承了Model类后重写的方法
     * 这个方法是为了mybatisplus的AR操作
     * @return
     */
    @Override
    protected Serializable pkVal() {
        return id;
    }
}
