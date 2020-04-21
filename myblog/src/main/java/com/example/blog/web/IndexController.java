package com.example.blog.web;

import com.example.blog.po.Blog;
import com.example.blog.po.Tag;
import com.example.blog.po.Type;
import com.example.blog.service.BlogService;
import com.example.blog.service.TagService;
import com.example.blog.service.TypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    BlogService blogService;

    @Autowired
    TypeService typeService;

    @Autowired
    TagService tagService;

    int typeNum = 5;//显示类型的个数
    int tagNum = 10;//显示标签的个数
    int blogNum = 5;//显示推荐的个数


    //主页
    @GetMapping("/")
    public String index(@RequestParam(required = false,defaultValue = "1",value = "pagenum")int pagenum, Model model){
        PageHelper.startPage(pagenum, 6);
        List<Blog> allBlog = blogService.getIndexBlog();
        //得到分页结果对象
        PageInfo<Blog> pageInfo = new PageInfo<>(allBlog);
        model.addAttribute("pageInfo", pageInfo);
        
        //类型排序
        List<Type> topBlogType = typeService.getTopBlogType();
        System.out.println(topBlogType);
        Collections.sort(topBlogType, new Comparator<Type>() {
            @Override
            public int compare(Type o1, Type o2) {
                return o2.getBlogs().size()-o1.getBlogs().size();
            }
        });
        topBlogType = topBlogType.size()>typeNum ? topBlogType.subList(0,typeNum):topBlogType;
        model.addAttribute("types",topBlogType);

        //标签
        List<Tag> blogTag = tagService.getBlogTag();
        Collections.sort(blogTag, new Comparator<Tag>() {
            @Override
            public int compare(Tag o1, Tag o2) {
                return o2.getBlogs().size()-o1.getBlogs().size();
            }
        });
        blogTag = blogTag.size()>tagNum ? blogTag.subList(0,tagNum):blogTag;
        model.addAttribute("tags",blogTag);

        //推荐
        List<Blog> allRecommendBlog = blogService.getAllRecommendBlog();
        allRecommendBlog = allRecommendBlog.size()>blogNum ? allRecommendBlog.subList(0,blogNum):allRecommendBlog;
        model.addAttribute("recommendBlogs",allRecommendBlog);

        return "index";
    }

    //博客详情
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model){
        model.addAttribute("blog",blogService.getConvertDetailedBlog(id));
        return "blog";
    }


    //搜索
    @PostMapping("/search")
    public String search(@RequestParam(required = false,defaultValue = "1",value = "pagenum")int pagenum,
                        @RequestParam String query ,
                         @RequestParam(required = false,defaultValue = "false") boolean isSearch,Model model){
        PageHelper.startPage(pagenum, 6);
        List<Blog> allBlog = blogService.getSearchBlog(query);
        //得到分页结果对象
        PageInfo<Blog> pageInfo = new PageInfo<>(allBlog);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("query",query);

        if (isSearch==true){
            return "search";
        }
        return "search :: blogList";
    }



}
