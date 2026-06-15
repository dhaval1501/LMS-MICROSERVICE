package com.lms.book_service.entity;

import com.lms.book_service.enums.BookStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String isbn;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "category_id")
    private Category category;

    private String publisher;

    private LocalDate publishedDate;

    private Integer totalCopies;

    private Integer availableCopies;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}