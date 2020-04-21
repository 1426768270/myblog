package com.example.blog.web;

import com.example.blog.po.Blog;
import com.example.blog.po.Type;
import com.example.blog.service.BlogService;
import com.example.blog.service.TypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sf.jsqlparser.statement.select.First;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TypeShowController {

    @Autowired
    TypeService typeService;

    @Autowired
    BlogService blogService;

    @GetMapping("/types/{id}")
    public String types(@RequestParam(required = false,defaultValue = "1",value = "pagenum")int pagenum,
                        @PathVariable Long id,
                        Model model){

        List<Type> types = typeService.getAllViewType();
        if (id==-1){
            id = types.get(0).getId();
        }
        model.addAttribute("types",types);

        PageHelper.startPage(pagenum, 6);
        List<Blog> allBlog = blogService.getByTypeId(id);
        //得到分页结果对象
        PageInfo<Blog> pageInfo = new PageInfo<>(allBlog);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("activeTypeId",id);

        return "types";
    }
    @PostMapping("/types")
    public String postTypes(@RequestParam(required = false,defaultValue = "1",value = "pagenum")int pagenum,
                        @RequestParam Long id ,
                        Model model){

        PageHelper.startPage(pagenum, 6);
        List<Blog> allBlog = blogService.getByTypeId(id);
        //得到分页结果对象
        PageInfo<Blog> pageInfo = new PageInfo<>(allBlog);
        model.addAttribute("pageInfo", pageInfo);

        return "types :: typeList";
    }

}
