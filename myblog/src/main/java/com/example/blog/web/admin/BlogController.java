package com.example.blog.web.admin;

import com.example.blog.po.Blog;
import com.example.blog.po.BlogQuery;
import com.example.blog.po.Tag;
import com.example.blog.po.User;
import com.example.blog.service.BlogService;
import com.example.blog.service.TagService;
import com.example.blog.service.TypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT="admin/blogs-input";
    private static final String LIST="admin/blogs";
    private static final String REDIRECT_LIST="redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String list(@RequestParam(required = false,defaultValue = "1",value = "pagenum")int pagenum, Model model){
        PageHelper.startPage(pagenum, 5);
        List<Blog> allBlog = blogService.getAllBlog();

        //得到分页结果对象
        PageInfo<Blog> pageInfo = new PageInfo<>(allBlog);
        model.addAttribute("pageInfo", pageInfo);

        //获得类型
        model.addAttribute("types",typeService.getAllType());
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(@RequestParam(required = false,defaultValue = "1",value = "pagenum")int pagenum, BlogQuery blogQuery,Model model){

        PageHelper.startPage(pagenum, 5);
        List<Blog> allBlog = blogService.searchAllBlog(blogQuery);
        //得到分页结果对象
        PageInfo<Blog> pageInfo = new PageInfo<>(allBlog);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/blogs :: blogList";
    }

//    新增页面
    @GetMapping("/blogs/input")
    public String input(Model model){
        setTypeAndTag(model);

        model.addAttribute("blog",new Blog());
        return INPUT;
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        setTypeAndTag(model);

        Blog blog = blogService.getDetailedBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return INPUT;
    }

    //设置标签和属性
    public void setTypeAndTag(Model model){
        //获得type
        model.addAttribute("types",typeService.getAllType());
        //获得tag
        model.addAttribute("tags",tagService.getAllTag());

    }

    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes,HttpSession session){
        //设置user属性
        blog.setUser((User) session.getAttribute("user"));
        //设置用户id
        blog.setUserId(blog.getUser().getId());
        //设置blog的type
        blog.setType(typeService.getType(blog.getType().getId()));
        //设置blog中typeId属性
        blog.setTypeId(blog.getType().getId());
        //给blog中的List<Tag>赋值
        blog.setTags(tagService.getTagByString(blog.getTagIds()));

        if (blog.getId() == null) {   //id为空，则为新增
            int b = blogService.saveBlog(blog);
            if (b != 0){
                attributes.addFlashAttribute("msg", "新增成功");
            }else {
                attributes.addFlashAttribute("msg", "新增失败");
            }
        } else {
            int b = blogService.updateBlog(blog);
            if (b != 0){
                attributes.addFlashAttribute("msg", "修改成功");
            }else {
                attributes.addFlashAttribute("msg", "修改失败");
            }
        }
        return REDIRECT_LIST;
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("msg","删除成功");
        return REDIRECT_LIST;
    }


}
