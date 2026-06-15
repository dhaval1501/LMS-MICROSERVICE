package com.lms.bookservice.dto.author;

import lombok.Data;


import java.time.LocalDateTime;

@Data
public class AuthorResponseDTO {

    private Long id;

    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}