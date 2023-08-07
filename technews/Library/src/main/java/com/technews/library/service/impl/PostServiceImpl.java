package com.technews.library.service.impl;

import com.technews.library.dto.PostDto;
import com.technews.library.model.Category;
import com.technews.library.model.Post;
import com.technews.library.repository.PostRepository;
import com.technews.library.service.PostService;
import com.technews.library.utils.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ImageUpload imageUpload;

    @Override
    public List<PostDto> findALl() {
        List<Post> posts = postRepository.findAll();
        List<PostDto> postDtoList = transfer(posts);

        return postDtoList;
    }

    @Override
    public Post save(MultipartFile imagePost, PostDto postDto) {
        try {
            Post post = new Post();
            if (imagePost == null){
                post.setImage(null);
            }else {
                if (imageUpload.uploadImage(imagePost)){
                    System.out.println("Upload successfully");
                }
                post.setImage(Base64.getEncoder().encodeToString(imagePost.getBytes()));
            }
            post.setName(postDto.getName());
            post.setContent(postDto.getContent());
            post.setCategory(postDto.getCategory());
            post.setTime(postDto.getTime());
            post.set_activated(true);
            post.set_deleted(false);
            return postRepository.save(post);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public Post update(MultipartFile imagePost, PostDto postDto) {
        try {
            Post post = postRepository.getById(postDto.getId());
            if (imagePost == null){
                post.setImage(post.getImage());
            }else {
                if (imageUpload.checkExisted(imagePost) == false){
                    imageUpload.uploadImage((imagePost));
                }
                post.setImage(Base64.getEncoder().encodeToString(imagePost.getBytes()));
            }
            post.setName(postDto.getName());
            post.setContent(postDto.getContent());
            post.setTime(postDto.getTime());
            post.setCategory(postDto.getCategory());
            return postRepository.save(post);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteById(Long id) {
        Post post = postRepository.getById(id);
        post.set_deleted(true);
        post.set_activated(false);
        postRepository.save(post);
    }

    @Override
    public void enableById(Long id) {
        Post post = postRepository.getById(id);
        post.set_activated(true);
        post.set_deleted(false);
        postRepository.save(post);
    }

    @Override
    public PostDto getById(Long id) {
        Post post = postRepository.getById(id);
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setName(post.getName());
        postDto.setContent(post.getContent());
        postDto.setCategory(post.getCategory());
        postDto.setTime(post.getTime());
        postDto.setImage(post.getImage());
        postDto.setDeleted(post.is_deleted());
        postDto.setActivated(post.is_activated());
        return postDto;
    }

    @Override
    public Page<PostDto> pagePosts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<PostDto> posts = transfer(postRepository.findAll());
        Page<PostDto> postPages = toPage(posts, pageable);
        return postPages;
    }

    @Override
    public Page<PostDto> pagePostsHome(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 12);
        List<PostDto> posts = transfer(postRepository.findAll());
        Page<PostDto> postPagesHome = toPage(posts, pageable);
        return postPagesHome;
    }

    @Override
    public Page<PostDto> searchPosts(int pageNo, String keyword) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<PostDto> postDtoList = transfer(postRepository.searchPostsList(keyword));
        Page<PostDto> posts = toPage(postDtoList, pageable);
        return posts;
    }


    private Page toPage(List<PostDto> list, Pageable pageable){
        if (pageable.getOffset() >= list.size()){
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList, pageable, list.size());
    }

    private List<PostDto> transfer(List<Post> posts){
        List<PostDto> postDtoList = new ArrayList<>();
        for (Post post : posts){
            PostDto postDto = new PostDto();
            postDto.setId(post.getId());
            postDto.setName(post.getName());
            postDto.setContent(post.getContent());
            postDto.setCategory(post.getCategory());
            postDto.setTime(post.getTime());
            postDto.setImage(post.getImage());
            postDto.setDeleted(post.is_deleted());
            postDto.setActivated(post.is_activated());
            postDtoList.add(postDto);
        }
        return postDtoList;
    }

//    Customer

    @Override
    public List<Post> getAllPosts() {
        return postRepository.getAllPosts();
    }
    @Override
    public Post getPostById(Long id) {
        return postRepository.getById(id);
    }

    @Override
    public List<Post> getRelatedPosts(Long categoryId) {
        return postRepository.getRelatedPosts(categoryId);
    }

    @Override
    public List<Post> getPostsInCategory(Long categoryId) {
        return postRepository.getPostsInCategory(categoryId);
    }


    @Override
    public Page<Post> findAllPostsByPage(int page, int size){
        Pageable pageRender = PageRequest.of(page - 1, size);
//        return postRepository.findAll(pageRender);
        return  postRepository.pagePostHome(pageRender);
    }

    @Override
    public List<Post> getPostLatest() {
        return postRepository.getPostLatest();
    }
}
