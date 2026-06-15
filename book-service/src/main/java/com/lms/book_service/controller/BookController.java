package com.lms.book_service.controller;

import com.lms.book_service.dto.book.BookRequestDTO;
import com.lms.book_service.dto.book.BookResponseDTO;
import com.lms.book_service.dto.book.SearchBookRequestDTO;
import com.lms.book_service.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
//@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @PostMapping("/add")
    @Operation(summary = "Add new Book with new Category or Author")
    public ResponseEntity<BookResponseDTO> addBook(@Valid @RequestBody BookRequestDTO requestDTO){
        BookResponseDTO responseDTO=bookService.createBook(requestDTO);
       return new ResponseEntity<BookResponseDTO>(responseDTO, HttpStatus.CREATED);
    }
    @PostMapping("/search")
    @Operation(summary = "Search Books with Category or Author")
    public Page<BookResponseDTO> searchBooks(@RequestBody SearchBookRequestDTO searchDTO) {

        return bookService.searchBooks(searchDTO);
    }
//
//    @PutMapping("/update/{id}")
//    @CrossOrigin( origins = "http://Localhost:4200")
//    public Book updateBook(@PathVariable Long id,@RequestBody Book book){
//      return  bookCommonService.update(id,book);
//
//    }
//
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Book")
    public ResponseEntity<?>  deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/byId/{id}")
    @Operation(summary = "Get Book by Id")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id){
        BookResponseDTO responseDTO =  bookService.getBookById(id);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);

    }

    @PostMapping("/issue/{id}")
    public ResponseEntity<BookResponseDTO> issueBook(@PathVariable Long id){
        BookResponseDTO responseDTO =  bookService.issueBook(id);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @PostMapping("/return/{id}")
    public ResponseEntity<BookResponseDTO> returnBook(@PathVariable Long id){
        BookResponseDTO responseDTO =  bookService.returnBook(id);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

}
