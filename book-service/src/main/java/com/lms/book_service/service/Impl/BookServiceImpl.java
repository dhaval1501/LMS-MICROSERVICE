package com.lms.book_service.service.Impl;

import com.lms.book_service.dto.book.BookRequestDTO;
import com.lms.book_service.dto.book.BookResponseDTO;
import com.lms.book_service.dto.book.BookSearchDTO;
import com.lms.book_service.entity.Author;
import com.lms.book_service.entity.Book;
import com.lms.book_service.entity.Category;
import com.lms.book_service.mapper.BookMapper;
import com.lms.book_service.repository.AuthorRepository;
import com.lms.book_service.repository.BookRepository;
import com.lms.book_service.repository.CategoryRepository;
import com.lms.book_service.repository.specification.BookSpecification;
import com.lms.book_service.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;


    @Override
    public BookResponseDTO getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(()->
                new RuntimeException("Book not found with id: " + id)
        );

        return bookMapper.toDto(book);
    }

    @Override
    public Page<BookResponseDTO> getAllPageableBooks(BookSearchDTO searchDTO) {
        Pageable pageable = setPageable(searchDTO);

         return bookRepository
                .findAll(BookSpecification.getSpecification(searchDTO), pageable)
                .map(bookMapper::toDto);
    }

    private  Pageable setPageable(BookSearchDTO searchDTO){

        int page = searchDTO.getPage() != null ? searchDTO.getPage() : 0;
        int size = searchDTO.getSize() != null ? searchDTO.getSize() : 10;

        String sortBy= StringUtils.hasText(searchDTO.getSortBy()) ? searchDTO.getSortBy() : "id";

        Sort.Direction direction = Sort.Direction.DESC;

        if (StringUtils.hasText(searchDTO.getSortDirection())) {
            direction = Sort.Direction.fromString(
                    searchDTO.getSortDirection().toUpperCase()
            );
        }
        Sort sorting = Sort.by(
                Sort.Order.by(sortBy).with(direction)
        );

        return PageRequest.of(page,size,sorting);
    }


    @Override
    public BookResponseDTO createBook(BookRequestDTO requestDTO) {
        Book book = bookMapper.toEntity(requestDTO);
        book.setAvailableCopies(book.getTotalCopies());
        if(requestDTO.getAuthorId()!=null){
            Author persistAuthor=authorRepository.findById(requestDTO.getAuthorId())
                    .orElseThrow(()->new EntityNotFoundException("Author not found with id: " + requestDTO.getAuthorId()));
            book.setAuthor(persistAuthor);
            persistAuthor.getBookList().add(book);
        }
        if (requestDTO.getCategoryId()!=null){
            Category persistCategory=categoryRepository.findById(requestDTO.getCategoryId())
                    .orElseThrow(()->new EntityNotFoundException("Category not fount with id: " + requestDTO.getCategoryId()));
            book.setCategory(persistCategory);
            persistCategory.getBookList().add(book);
        }
        if(requestDTO.getAuthorName()!=null && requestDTO.getAuthorId() == null){
            Author author=new Author();
            author.setName(requestDTO.getAuthorName());
            author.getBookList().add(book);
            book.setAuthor(author);
        }
        if (requestDTO.getCategoryName()!=null && requestDTO.getCategoryId()== null){
            Category category=new Category();
            category.setName(requestDTO.getCategoryName());
            category.setDescription(requestDTO.getCategoryDescription());
            category.getBookList().add(book);
            book.setCategory(category);
        }

        return bookMapper.toDto(bookRepository.save(book));
    }



    @Override
    public BookResponseDTO updateBook(BookRequestDTO requestDTO) {
        return null;
    }

    @Override
    public BookResponseDTO issueBook(Long id) {
        Book book= bookRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException("Book not found with id: "+id)
        );
        if(book.getAvailableCopies()<=0){
            throw new IllegalStateException("No copies available");
        }
        book.setAvailableCopies(book.getAvailableCopies()-1);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookResponseDTO returnBook(Long id) {
        Book book= bookRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException("Book not found with id: "+id)
        );
        if (book.getAvailableCopies() >= book.getTotalCopies()) {
            throw new IllegalStateException("Cannot exceed total copies");
        }

        book.setAvailableCopies(book.getAvailableCopies()+1);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        Book book=bookRepository.findById(id).orElseThrow(
                ()->new  EntityNotFoundException("Book not found with id: "+id)
        );
        bookRepository.delete(book);
    }

    @Override
    public List<BookResponseDTO> getAllBooks(BookSearchDTO searchDTO) {
        return bookRepository
                .findAll(BookSpecification.getSpecification(searchDTO))
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookResponseDTO> getBookByIds(Set<Long> ids) {
        return bookRepository
                .findByIdIn(ids)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
