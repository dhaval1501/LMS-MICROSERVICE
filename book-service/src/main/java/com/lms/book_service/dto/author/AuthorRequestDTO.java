package com.lms.bookservice.dto.author;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class AuthorRequestDTO {

    @NotBlank(message = "Author name is required")
    private String name;
}