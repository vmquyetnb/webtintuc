package com.technews.library.service;

import com.technews.library.dto.PostDto;
import com.technews.library.model.Category;
import com.technews.library.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    List<PostDto> findALl();
    Post save(MultipartFile imagePost, PostDto postDto);
    Post update(MultipartFile imagePost, PostDto postDto);
    void deleteById(Long id);
    void enableById(Long id);

    PostDto getById(Long id);

    Page<PostDto> pagePosts(int pageNo);

    Page<PostDto> pagePostsHome(int pageNo);

    Page<PostDto> searchPosts(int pageNo, String keyword);

    // Customer


    List<Post> getAllPosts();
    Post getPostById(Long id);

    List<Post> getRelatedPosts(Long categoryId);

    List<Post> getPostsInCategory(Long categoryId);

    Page<Post> findAllPostsByPage(int page, int size);

    List<Post> getPostLatest();

}
