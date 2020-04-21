package com.example.blog.service.Impl;

import com.example.blog.dao.UserDao;
import com.example.blog.po.User;
import com.example.blog.service.UserService;
import com.example.blog.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User checkUser(String username, String password){

        User user = userDao.findByUsernameAndPassword(username, MD5Util.code(password));
        return user;
    }
}
