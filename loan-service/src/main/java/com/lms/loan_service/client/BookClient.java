package com.lms.loan_service.client;

import com.lms.loan_service.dto.book.BookResponseDTO;
import com.lms.loan_service.dto.loan.LoanSearchDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@FeignClient(value = "book-service")
public interface BookClient {

    @GetMapping("api/v1/books/byId/{id}")
    BookResponseDTO getBookById(@PathVariable Long id);

    @PostMapping("api/v1/books/issue/{id}")
    BookResponseDTO issueBook(@PathVariable Long id);

    @PostMapping("api/v1/books/return/{id}")
    BookResponseDTO returnBook(@PathVariable Long id);

    @PostMapping("api/v1/books/bulk")
    List<BookResponseDTO> getAllBooks(@RequestBody BookResponseDTO searchDTO);

    @PostMapping("api/v1/books/by-ids")
    List<BookResponseDTO> getBookByIds(@RequestBody Set<Long> ids);

}
