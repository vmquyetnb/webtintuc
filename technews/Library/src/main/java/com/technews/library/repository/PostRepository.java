package com.technews.library.repository;

import com.technews.library.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p")
    Page<Post> pagePost(Pageable pageable);

    @Query("select p from Post p order by p.id desc ")
    Page<Post> pagePostHome(Pageable pageable);

    @Query(value = "select * from posts p order by p.post_id desc limit 10", nativeQuery = true)
    List<Post> getPostLatest();

    @Query("select p from Post p where p.content like %?1% or p.name like %?1% or p.category.name like %?1%")
    Page<Post> searchPosts(String keyword, Pageable pageable);

    @Query("select p from Post p where p.content like %?1% or p.name like %?1% or p.category.name like %?1%")
    List<Post> searchPostsList(String keyword);

    //    Customer
    @Query("select p from Post p where p.is_activated = true and p.is_deleted = false")
    List<Post> getAllPosts();

    @Query(value = "select * from posts p inner join categories c on c.category_id = p.category_id where p.category_id = ?1 order by rand(p.post_id) asc limit 8", nativeQuery = true)
    List<Post> getRelatedPosts(Long categoryId);

    @Query(value = "select p from Post p inner join Category c on c.id = p.category.id where c.id = ?1 and p.is_deleted = false and p.is_activated = true" )
    List<Post> getPostsInCategory(Long categoryId);
}
