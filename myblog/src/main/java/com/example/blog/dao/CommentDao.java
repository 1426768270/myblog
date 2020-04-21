package com.example.blog.dao;

import com.example.blog.po.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentDao {

    //根据创建时间倒序来排
    List<Comment> findByBlogIdAndParentComment(@Param("blogId") Long blogId, @Param("blogParentId") Long blogParentId);

    //查询父级对象
    Comment findByParentCommentId(@Param("parentCommentId")Long parentcommentid);

    //添加一个评论
    int saveComment(Comment comment);

    Comment findOne(Long id);
}
