package com.technews.library.service;

import com.technews.library.dto.CategoryDto;
import com.technews.library.model.Category;
import com.technews.library.model.Post;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category save(Category category);
    Category findById(Long id);
    Category update(Category category);
    void deleteById(Long id);
    void enableById(Long id);

    List<Category> findAllByActivated();

//    Customer
    List<CategoryDto> getCategoryAndPost();


}
