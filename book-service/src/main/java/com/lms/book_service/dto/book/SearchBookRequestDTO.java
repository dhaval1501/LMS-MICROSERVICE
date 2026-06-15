package com.lms.book_service.dto.book;

import lombok.Data;

@Data
public class SearchBookRequestDTO {

    private String title;

    private Long authorId;

    private Long categoryId;

    private String isbn;

    private Integer page = 0;

    private Integer size = 10;

    private String sortBy = "id";

    private String sortDirection = "asc";
}