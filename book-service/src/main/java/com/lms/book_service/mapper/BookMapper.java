package com.lms.book_service.mapper;

import com.lms.book_service.dto.book.BookResponseDTO;
import com.lms.book_service.entity.Book;
import org.mapstruct.Mapper;
import com.lms.book_service.dto.book.BookRequestDTO;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

//    @Mappings({
            @Mapping(source = "author.name",target = "authorName")
            @Mapping(source = "author.id",target = "authorId")
            @Mapping(source = "category.name",target = "categoryName")
            @Mapping(source = "category.id",target = "categoryId")
//    })
    BookResponseDTO toDto(Book book);

    List<BookResponseDTO> toListDto(List<Book> bookList);

    @Mappings({
            @Mapping(target = "author",ignore = true),
            @Mapping(target = "category",ignore = true)
    })
    Book toEntity(BookRequestDTO requestDTO);

}
