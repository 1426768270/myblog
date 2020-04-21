package com.example.blog.dao;

import com.example.blog.po.Tag;

import java.util.List;

public interface TagDao {

    int saveTag(Tag tag);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    List<Tag> getAllTag();

    List<Tag> getBlogTag();  //首页右侧展示tag对应的博客数量

    int updateTag(Tag tag);

    int deleteTag(Long id);


}
