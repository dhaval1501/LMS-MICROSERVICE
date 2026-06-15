package com.lms.book_service.repository;

import com.lms.book_service.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Long>, JpaSpecificationExecutor<Book> {


//    Page<Book> findAll(Pageable pageable);

}
