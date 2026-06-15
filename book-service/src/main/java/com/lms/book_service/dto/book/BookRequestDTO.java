package com.lms.book_service.dto.book;

import com.lms.book_service.enums.BookStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.time.LocalDate;

@Data
public class BookRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    private String isbn;

    private String authorName;

    private Long authorId;

    private Long categoryId;

    private String categoryName;

    private String categoryDescription;

    private String publisher;

    private LocalDate publishedDate;

    @NotNull(message = "Total copies is required")
    private Integer totalCopies;

    private Integer availableCopies;

    private BookStatus status;
}