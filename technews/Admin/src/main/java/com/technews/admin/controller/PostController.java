package com.technews.admin.controller;

import com.technews.library.dto.PostDto;
import com.technews.library.model.Category;
import com.technews.library.model.Post;
import com.technews.library.service.CategoryService;
import com.technews.library.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/posts")
    public String posts(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<PostDto> postDtoList = postService.findALl();
        model.addAttribute("title", "Manage Post");
        model.addAttribute("posts", postDtoList);
        model.addAttribute("size", postDtoList.size());
        return "posts";
    }

    @GetMapping("/posts/{pageNo}")
    public String postsPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        Page<PostDto> posts = postService.pagePosts(pageNo);
        model.addAttribute("title", "Manage Post");
        model.addAttribute("size", posts.getSize());
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("posts", posts);
        return "posts";
    }

    @GetMapping("/search-result/{pageNo}")
    public String searchPosts(@PathVariable("pageNo")int pageNo,
                              @RequestParam("keyword")String keyword,
                              Model model,
                              Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        Page<PostDto> posts = postService.searchPosts(pageNo, keyword);
        model.addAttribute("title", "Search Result");
        model.addAttribute("posts", posts);
        model.addAttribute("size", posts.getSize());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", posts.getTotalPages());
        return "result-posts";
    }

    @GetMapping("/add-post")
    public String addPostForm(Model model){
        List<Category> categories = categoryService.findAllByActivated();
        model.addAttribute("categories", categories);
        model.addAttribute("post", new PostDto());
        return "add-post";
    }

    @PostMapping("/save-post")
    public String savePost(@ModelAttribute("post") PostDto postDto,
                           @RequestParam("imagePost")MultipartFile imagePost,
                           RedirectAttributes attributes){
    try {
        postService.save(imagePost, postDto);
        attributes.addFlashAttribute("success", "Add successfully");
    }catch (Exception e){
        e.printStackTrace();
        attributes.addFlashAttribute("error", "Failed to add");
    }

        return "redirect:/posts/0";
    }

    @GetMapping("/update-post/{id}")
    public String updatePostForm(@PathVariable("id") Long id, Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        model.addAttribute("title", "Update posts");
        List<Category> categories = categoryService.findAllByActivated();
        PostDto postDto = postService.getById(id);
        model.addAttribute("categories", categories);
        model.addAttribute("postDto", postDto);
        return "update-post";
    }

    @PostMapping("/update-post/{id}")
    public String processUpdate(@PathVariable("id") Long id,
                                @ModelAttribute("postDto") PostDto postDto,
                                @RequestParam("imagePost")MultipartFile imagePost,
                                RedirectAttributes attributes
    ){
        try {
            postService.update(imagePost, postDto);
            attributes.addFlashAttribute("success", "Update successfully");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to update");
        }
        return "redirect:/posts/0";
    }

    @RequestMapping(value = "/enable-post/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enabledPost(@PathVariable("id") Long id, RedirectAttributes attributes){
        try {
            postService.enableById(id);
            attributes.addFlashAttribute("success", "Enabled successfully");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to enabled");
        }
        return "redirect:/posts/0";
    }

    @RequestMapping(value = "/delete-post/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deletedPost(@PathVariable("id") Long id, RedirectAttributes attributes){
        try {
            postService.deleteById(id);
            attributes.addFlashAttribute("success", "Deleted successfully");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to deleted");
        }
        return "redirect:/posts/0";
    }


}
