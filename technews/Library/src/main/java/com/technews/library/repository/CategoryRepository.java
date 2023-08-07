package com.technews.library.repository;

import com.technews.library.dto.CategoryDto;
import com.technews.library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c where c.is_activated = true and c.is_deleted = false ")
    List<Category> findAllByActivated();

//    Customer
    @Query("select new com.technews.library.dto.CategoryDto(c.id, c.name, count(p.category.id)) from Category c inner join Post p on p.category.id = c.id " +
            " where  c.is_activated = true and c.is_deleted = false group by c.id")
    List<CategoryDto> getCategoryAndPost();
}
