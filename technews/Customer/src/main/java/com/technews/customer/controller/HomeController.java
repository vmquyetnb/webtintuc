package com.technews.customer.controller;

import com.technews.library.dto.CategoryDto;
import com.technews.library.dto.PostDto;
import com.technews.library.model.Category;
import com.technews.library.model.Post;
import com.technews.library.service.CategoryService;
import com.technews.library.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private PostService postService;

    @Autowired
    private CategoryService categoryService;

//    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
//    public String index(Model model){
//
//        return "index";
//    }

    @GetMapping("/index")
    public String index(Model model){
        List<Category> categories = categoryService.findAll();
        List<PostDto> postDtos = postService.findALl();
        List<Post> postes= postService.getPostLatest();
        model.addAttribute("postes", postes);
        model.addAttribute("categories", categories);
        model.addAttribute("posts", postDtos);
        return "index";
    }

    @GetMapping("/posts")
    public String posts(Model model){
        List<CategoryDto> categoryDtoList = categoryService.getCategoryAndPost();
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("categories", categoryDtoList);
        model.addAttribute("posts", posts);
        return "index";
    }


    @GetMapping("/find-post/{id}")
    public String findPostById(@PathVariable("id") Long id, Model model){
        Post post = postService.getPostById(id);
        Long categoryId = post.getCategory().getId();
        List<Post> posts = postService.getRelatedPosts(categoryId);
        List<Category> categoriess = categoryService.findAll();
        model.addAttribute("categoriess", categoriess);
        model.addAttribute("post", post);
        model.addAttribute("posts", posts);
        return "detail";
    }

    @GetMapping("/posts-in-category/{id}")
    public String getPostsInCategory(@PathVariable("id") Long id, Model model){
        Category category = categoryService.findById(id);
        Long categoryId = category.getId();
        List<CategoryDto> categories = categoryService.getCategoryAndPost();
        List<Post> posts = postService.getPostsInCategory(categoryId);
        List<Category> categoriess = categoryService.findAll();
        List<Post> postsss = postService.getRelatedPosts(categoryId);
        model.addAttribute("categoriess", categoriess);
        model.addAttribute("category", category);
        model.addAttribute("categories", categories);
        model.addAttribute("posts", posts);
        model.addAttribute("postsss", postsss);
        return "posts-in-category";
    }

    @GetMapping("/postRelated-category")
    public String postRelatedcategory(@RequestParam(value = "page", defaultValue = "1") int pageNo,
                                      @PathVariable("id") Long id,
                                      Model model){
        Category category = categoryService.findById(id);
        Long categoryId = category.getId();
        Page<Post> posts = postService.findAllPostsByPage(pageNo, 10);
        List<Post> postssss = postService.getPostsInCategory( categoryId);
        model.addAttribute("title", "Manage Post");
        model.addAttribute("size", posts.getSize());
        model.addAttribute("totalPagesHome", posts.getTotalPages());
        model.addAttribute("currentPageHome", pageNo);
        model.addAttribute("postssss", postssss);
        return "posts-in-category";
    }

//    @GetMapping("/posts-in-category/{id}")
//    public String getPostsInCategory(@PathVariable("id") Long categoryId, Model model){
//        Category category = categoryService.findById(categoryId);
//        List<CategoryDto> categories = categoryService.getCategoryAndPost();
//        List<Post> posts = postService.getPostsInCategory(categoryId);
//        model.addAttribute("category", category);
//        model.addAttribute("categories", categories);
//        model.addAttribute("posts", posts);
//        return "posts-in-category";
//    }

//    @GetMapping("/find-posts/{id}")
//    public String findPostsById(@PathVariable("id") Long id, Model model){
//        Post post = postService.getPostById(id);
//        Long categoryId = post.getCategory().getId();
//        List<Post> posts = postService.getRelatedPosts(categoryId);
//        model.addAttribute("post", post);
//        model.addAttribute("posts", posts);
//        return "posts-in-category";
//    }


    @GetMapping("/post")
    public String post(Model model) {

        List<PostDto> postDtoList = postService.findALl();
        model.addAttribute("title", "Manage Post");
        model.addAttribute("posts", postDtoList);
        model.addAttribute("size", postDtoList.size());
        return "result-post";
    }

    @GetMapping("/posts/{pageNo}")
    public String postsPage(@PathVariable("pageNo") int pageNo, Model model){
        Page<PostDto> posts = postService.pagePosts(pageNo);
        model.addAttribute("title", "Manage Post");
        model.addAttribute("size", posts.getSize());
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("posts", posts);
        return "result-post";
    }

    @GetMapping("/page-index")
    public String postsPageHome(@RequestParam(value = "page", defaultValue = "1") int pageNo, Model model){
        Page<Post> posts = postService.findAllPostsByPage(pageNo, 10);
        List<Category> categoriess = categoryService.findAll();
        model.addAttribute("categoriess", categoriess);
        model.addAttribute("title", "Manage Post");
        model.addAttribute("size", posts.getSize());
        model.addAttribute("totalPagesHome", posts.getTotalPages());
        model.addAttribute("currentPageHome", pageNo);
        model.addAttribute("posts", posts);
        return "post-latest";
    }

//    @GetMapping("/search-results/{pageNo}")
//    public String searchPosts(@PathVariable("pageNo")int pageNo,
//                              @RequestParam("keyword")String keyword,
//                              Model model){
//
//        Page<PostDto> posts = postService.searchPosts(pageNo, keyword);
//        model.addAttribute("title", "Search Result");
//        model.addAttribute("posts", posts);
//        model.addAttribute("size", posts.getSize());
//        model.addAttribute("currentPage", pageNo);
//        model.addAttribute("totalPages", posts.getTotalPages());
//        return "result-post";
//    }
        @GetMapping("/search-results")
        public String searchPosts(@RequestParam(value = "pageNo", defaultValue = "0")int pageNo,
                                    @RequestParam("keyword")String keyword,
                                    Model model){

        Page<PostDto> posts = postService.searchPosts(pageNo, keyword);
        List<Category> categoriess = categoryService.findAll();
        model.addAttribute("categoriess", categoriess);
        model.addAttribute("title", "Search Result");
        model.addAttribute("posts", posts);
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", posts.getSize());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", posts.getTotalPages());
        return "result-post";
    }

    @GetMapping("/about")
    public String about(Model model){
        List<Category> categoriess = categoryService.findAll();
        model.addAttribute("categoriess", categoriess);
        return "about";
    }

}
