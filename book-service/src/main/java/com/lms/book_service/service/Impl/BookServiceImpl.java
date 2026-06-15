package com.lms.book_service.service.Impl;

import com.lms.book_service.dto.book.BookRequestDTO;
import com.lms.book_service.dto.book.BookResponseDTO;
import com.lms.book_service.dto.book.SearchBookRequestDTO;
import com.lms.book_service.entity.Author;
import com.lms.book_service.entity.Book;
import com.lms.book_service.entity.Category;
import com.lms.book_service.mapper.BookMapper;
import com.lms.book_service.repository.AuthorRepository;
import com.lms.book_service.repository.BookRepository;
import com.lms.book_service.repository.CategoryRepository;
import com.lms.book_service.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
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
    public Page<BookResponseDTO> searchBooks(SearchBookRequestDTO searchDTO) {
        Sort sort = searchDTO.getSortBy().equalsIgnoreCase("desc")
                ? Sort.by(searchDTO.getSortBy()).descending()
                : Sort.by(searchDTO.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), sort);

        Specification<Book> specification =
               this.searchBooksSpe(searchDTO,pageable);

         return bookRepository
                .findAll(specification, pageable)
                .map(bookMapper::toDto);
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

    public  Specification<Book> searchBooksSpe(SearchBookRequestDTO dto, Pageable pageable) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("title")),
                                "%" + dto.getTitle().toLowerCase() + "%"
                        )
                );
            }

            if (dto.getAuthorId() != null) {
                predicates.add(
                        cb.equal(
                                root.get("author").get("id"),
                                dto.getAuthorId()
                        )
                );
            }

            if (dto.getCategoryId() != null) {
                predicates.add(
                        cb.equal(
                                root.get("category").get("id"),
                                dto.getCategoryId()
                        )
                );
            }

            if (dto.getIsbn() != null && !dto.getIsbn().isBlank()) {
                predicates.add(
                        cb.equal(root.get("isbn"), dto.getIsbn())
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
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
}
