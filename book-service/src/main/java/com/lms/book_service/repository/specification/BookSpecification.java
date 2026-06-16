package com.lms.book_service.repository.specification;

import com.lms.book_service.dto.book.BookSearchDTO;
import com.lms.book_service.entity.Author;
import com.lms.book_service.entity.Book;
import com.lms.book_service.entity.Category;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class BookSpecification {

    public static Specification<Book> getSpecification(BookSearchDTO searchDTO){

        return (r,q,cb)->{
            if(searchDTO == null){
                cb.conjunction();
            }
            List<Predicate> predicates =new ArrayList<>();

//            if (!searchDTO.getIds().isEmpty()){
//                predicates.add(r.get("id").in(searchDTO.getIds()));
//            }
            if(searchDTO.getIsbn()!=null && StringUtils.hasText(searchDTO.getIsbn())){
                predicates.add(cb.like(cb.lower(r.get("isbn")),"%"+searchDTO.getIsbn()+"%"));
            }
            if(searchDTO.getTitle()!=null && StringUtils.hasText(searchDTO.getTitle())){
                predicates.add(cb.like(cb.lower(r.get("title")),"%"+searchDTO.getTitle()+"%"));
            }
            if(searchDTO.getPublisher()!=null && StringUtils.hasText(searchDTO.getPublisher())){
                predicates.add(cb.like(cb.lower(r.get("publisher")),"%"+searchDTO.getPublisher()+"&"));
            }
            if(searchDTO.getPublishedDate()!=null ){
                predicates.add(cb.equal(cb.lower(r.get("publishedDate")),"%"+searchDTO.getPublishedDate()+"%"));
            }
            if(searchDTO.getAuthorName()!=null && StringUtils.hasText(searchDTO.getAuthorName())){
                Join<Book, Author> authorJoin= r.join("author", JoinType.INNER);
                predicates.add(cb.like(cb.lower(authorJoin.get("name")),"%"+searchDTO.getAuthorName()+"%"));

            }
            if(searchDTO.getCategoryName()!=null && StringUtils.hasText(searchDTO.getCategoryName())){
                Join<Book, Category> categoryJoin= r.join("category",JoinType.INNER);
                predicates.add(cb.like(categoryJoin.get("name"),searchDTO.getCategoryName()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

    }
}
