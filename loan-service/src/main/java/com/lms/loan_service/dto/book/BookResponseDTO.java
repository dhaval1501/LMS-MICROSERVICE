package com.lms.loan_service.dto.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookResponseDTO {
    private Long id;

    private String title;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer availableCopies;

    private String isbn;

    private String authorName;

    private String categoryName;

    private String publisher;

    private LocalDate publishedDate;
}
