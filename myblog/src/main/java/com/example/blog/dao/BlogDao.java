package com.example.blog.dao;

import com.example.blog.po.Blog;
import com.example.blog.po.BlogAndTag;
import com.example.blog.po.BlogQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlogDao {

    Blog getBlog(Long id);  //后台展示博客

    Blog getDetailedBlog(@Param("id") Long id);  //博客详情

    List<Blog> getAllBlog();

    List<Blog> getByTypeId(Long typeId);  //根据类型id获取博客

    List<Blog> getByTagId(Long tagId);  //根据标签id获取博客

    List<Blog> getIndexBlog();  //主页博客展示

    List<Blog> getAllRecommendBlog();  //推荐博客展示

    List<Blog> getSearchBlog(String query);  //全局搜索博客

    List<Blog> searchAllBlog(BlogQuery blogQuery);  //后台根据标题、分类、推荐搜索博客

    List<String> findGroupYear();  //查询所有年份，返回一个集合

    List<Blog> findByYear(@Param("year") String year);  //按年份查询博客

    int removeTags(Long id);

    int saveBlog(Blog blog);

    int saveBlogAndTag(BlogAndTag blogAndTag);

    int updateBlog(Blog blog);

    int updateBlogView(Long id);

    int deleteBlog(Long id);

    int count();
}
