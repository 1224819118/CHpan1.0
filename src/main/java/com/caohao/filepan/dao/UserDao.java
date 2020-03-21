package com.caohao.filepan.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caohao.filepan.entity.User;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public interface UserDao extends BaseMapper<User> {
}
