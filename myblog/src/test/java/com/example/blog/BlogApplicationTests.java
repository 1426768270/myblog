package com.example.blog;

import com.example.blog.dao.TypeDao;
import com.example.blog.dao.UserDao;
import com.example.blog.po.Type;
import com.example.blog.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootTest
class BlogApplicationTests {

    @Autowired
    TypeDao typeDao;

    @Autowired
    UserDao userDao;


    @Test
    void contextLoads() {
        Type type = typeDao.getType((long) 1);
        System.out.println(type);

    }

}
