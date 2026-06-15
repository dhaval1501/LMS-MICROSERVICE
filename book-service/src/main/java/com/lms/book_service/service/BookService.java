package com.lms.book_service.service;

import com.lms.book_service.dto.book.BookRequestDTO;
import com.lms.book_service.dto.book.BookResponseDTO;
import com.lms.book_service.dto.book.SearchBookRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookService {

    BookResponseDTO getBookById(Long id);

    Page<BookResponseDTO> searchBooks(SearchBookRequestDTO searchDTO);


    BookResponseDTO createBook(BookRequestDTO requestDTO);

    BookResponseDTO updateBook(BookRequestDTO requestDTO);

    BookResponseDTO issueBook(Long id);

    BookResponseDTO returnBook(Long id);

    void deleteBook(Long id);


}
