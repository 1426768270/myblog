package com.example.blog.web;

import com.example.blog.po.Blog;
import com.example.blog.po.Tag;
import com.example.blog.service.BlogService;
import com.example.blog.service.TagService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    TagService tagService;

    @Autowired
    BlogService blogService;

    @GetMapping("/tags/{id}")
    public String tags(@RequestParam(required = false,defaultValue = "1",value = "pagenum")int pagenum,
                        @PathVariable Long id,
                        Model model){

        List<Tag> tags = tagService.getBlogTag();
        if (id==-1){
            id = tags.get(0).getId();
        }
        model.addAttribute("tags",tags);

        PageHelper.startPage(pagenum, 6);
        List<Blog> allBlog = blogService.getByTagId(id);

        //得到分页结果对象
        PageInfo<Blog> pageInfo = new PageInfo<>(allBlog);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("activeTagId",id);

        return "tags";
    }
    @PostMapping("/tags")
    public String postTypes(@RequestParam(required = false,defaultValue = "1",value = "pagenum")int pagenum,
                            @RequestParam Long id ,
                            Model model){

        PageHelper.startPage(pagenum, 6);
        List<Blog> allBlog = blogService.getByTagId(id);
        //得到分页结果对象
        PageInfo<Blog> pageInfo = new PageInfo<>(allBlog);
        model.addAttribute("pageInfo", pageInfo);

        return "tags :: tagList";
    }

}
