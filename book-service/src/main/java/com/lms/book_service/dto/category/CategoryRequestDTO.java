package com.lms.book_service.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequestDTO {

    @NotBlank(message = "Category name is required")
    private String name;

    private String description;
}