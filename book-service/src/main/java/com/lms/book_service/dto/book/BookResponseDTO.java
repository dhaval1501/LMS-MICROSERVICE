package com.lms.book_service.dto.book;

import com.lms.book_service.enums.BookStatus;
import lombok.Data;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookResponseDTO {

   private Long id;

   private String title;

   private String isbn;

   private String authorName;

   private Long authorId;

   private String categoryName;

   private Long categoryId;

   private String publisher;

   private LocalDate publishedDate;

   private Integer totalCopies;

   private Integer availableCopies;

   private BookStatus status;

   private LocalDateTime createdAt;

   private LocalDateTime updatedAt;
}