package com.example.blog.service.Impl;

import com.example.blog.dao.BlogDao;
import com.example.blog.dao.TagDao;
import com.example.blog.error.NotFoundException;
import com.example.blog.po.Blog;
import com.example.blog.po.BlogAndTag;
import com.example.blog.po.BlogQuery;
import com.example.blog.po.Tag;
import com.example.blog.service.BlogService;
import com.example.blog.util.MarkdownUtils;
import com.example.blog.util.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    BlogDao blogDao;


    @Override
    public Blog getBlog(Long id) {
        return blogDao.getBlog(id);
    }

    @Override
    public Blog getDetailedBlog(Long id) {
        return blogDao.getDetailedBlog(id);
    }

    @Transactional
    @Override
    public Blog getConvertDetailedBlog(Long id) {
        Blog blog = blogDao.getDetailedBlog(id);
        if (blog == null){
            throw new NullPointerException("该博客不存在");
        }
        //处理markdown
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content =b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        blogDao.updateBlogView(id);
        return b;

    }

    @Override
    public List<Blog> getAllBlog() {
        return blogDao.getAllBlog();
    }

    @Override
    public List<Blog> getByTypeId(Long typeId) {
        return blogDao.getByTypeId(typeId);
    }

    @Override
    public List<Blog> getByTagId(Long tagId) {
        return blogDao.getByTagId(tagId);
    }

    @Override
    public List<Blog> getIndexBlog() {
        return blogDao.getIndexBlog();
    }

    @Override
    public List<Blog> getAllRecommendBlog() {
        return blogDao.getAllRecommendBlog();
    }

    @Override
    public List<Blog> getSearchBlog(String query) {
        return blogDao.getSearchBlog(query);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogDao.findGroupYear();

        Map<String, List<Blog>> map = new LinkedHashMap<>();
        for (String year : years){
            map.put(year,blogDao.findByYear(year));
        }

        return map;
    }

    @Override
    public int countBlog() {
        return blogDao.count();
    }

    @Transactional
    @Override
    public int saveBlog(Blog blog) {
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViews(0);
        //保存博客
        blogDao.saveBlog(blog);
        //保存博客后才能获取自增的id
        Long id = blog.getId();
        //将标签的数据存到t_blogs_tag表中
        List<Tag> tags = blog.getTags();
        BlogAndTag blogAndTag = null;
        for (Tag tag : tags) {
            //新增时无法获取自增的id,在mybatis里修改
            blogAndTag = new BlogAndTag(tag.getId(), id);
            blogDao.saveBlogAndTag(blogAndTag);
        }
        return 1;
    }

    @Transactional
    @Override
    public int updateBlog(Blog blog) {
        Blog  b = blogDao.getBlog(blog.getId());
        if (b==null){
            throw new NotFoundException("该博客不存在");
        }
        blog.setUpdateTime(new Date());
        //将标签的数据存到t_blogs_tag表中
        List<Tag> tags = blog.getTags();
        BlogAndTag blogAndTag = null;

        //删除该博客的所有id
        blogDao.removeTags(blog.getId());

        for (Tag tag : tags) {
            blogAndTag = new BlogAndTag(tag.getId(), blog.getId());
            blogDao.saveBlogAndTag(blogAndTag);
        }

        //过滤掉为空的
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));

        return blogDao.updateBlog(b);
    }

    @Transactional
    @Override
    public int deleteBlog(Long id) {
        return blogDao.deleteBlog(id);
    }


    @Override
    public List<Blog> searchAllBlog(BlogQuery blogQuery) {
        return blogDao.searchAllBlog(blogQuery);
    }
}
