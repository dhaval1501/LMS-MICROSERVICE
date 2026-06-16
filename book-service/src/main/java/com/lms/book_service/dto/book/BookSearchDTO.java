package com.lms.book_service.dto.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class BookSearchDTO {
//    private List<Long> ids =new ArrayList<>();

    private String title;

//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    private Integer availableCopies;

    private String isbn;

    private String authorName;

    private String categoryName;

    private String publisher;

    private LocalDate publishedDate;

    private Integer page ;
    private Integer size ;
    private String sortBy ;
    private String sortDirection ;
}
