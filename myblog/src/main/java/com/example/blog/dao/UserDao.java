package com.example.blog.dao;

import com.example.blog.po.User;
import org.apache.ibatis.annotations.Param;


public interface UserDao {

    User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

}
