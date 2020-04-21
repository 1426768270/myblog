package com.example.blog.service.Impl;

import com.example.blog.dao.CommentDao;
import com.example.blog.po.Comment;
import com.example.blog.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentDao commentDao;

    @Override
    public List<Comment> getCommentByBlogId(Long blogId) {
        //没有父节点的默认为-1 把第一级的节点查出
        List<Comment> comments = commentDao.findByBlogIdAndParentComment(blogId, Long.parseLong("-1"));

        return  eachComment(comments);
    }

    @Transactional
    @Override
    public int saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1){
            //有父评论
            comment.setParentComment(commentDao.findByParentCommentId(parentCommentId));
            comment.setParentCommentId(parentCommentId);
        }else {
            //没有父评论
            comment.setParentCommentId((long) -1);
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentDao.saveComment(comment);
    }
    /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     *
     * @param comments root根节点，blog不为空的对象集合
     * @return
     */
    private void combineChildren(List<Comment> comments) {
        //查询表中的子数据集
        for (Comment comment : comments) {
            //从表中查出他的子数据

            List<Comment> replys1 = comment.getReplyComments();
            for(Comment reply1 : replys1) {

                //循环迭代，找出子代，存放在tempReplys中;
                recursively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();
    /**
     * 递归迭代，剥洋葱
     * @param comment 被迭代的对象
     * @return
     */
    private void recursively(Comment comment) {
        tempReplys.add(comment);//顶节点添加到临时存放集合
        //如果有子集

        if (comment.getReplyComments().size()>0) {
            List<Comment> replys = comment.getReplyComments();

            for (Comment reply : replys) {

                tempReplys.add(reply);
                if (reply.getReplyComments().size()>0) {
                    recursively(reply);
                }
            }
        }
    }
}
