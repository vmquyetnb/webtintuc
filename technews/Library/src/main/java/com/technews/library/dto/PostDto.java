package com.technews.library.dto;

import com.technews.library.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String name;
    private String content;
    private String image;
    private String time;
    private Category category;
    private boolean activated;
    private boolean deleted;
}
