package com.lms.loan_service.client;

import com.lms.loan_service.dto.book.BookResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@FeignClient(value = "book-service")
public interface BookClient {

    @GetMapping("api/v1/books/byId/{id}")
    BookResponseDTO getBookById(@PathVariable Long id);

    @PostMapping("api/v1/books/issue/{id}")
    BookResponseDTO issueBook(@PathVariable Long id);

    @PostMapping("api/v1/books/return/{id}")
    BookResponseDTO returnBook(@PathVariable Long id);

}
