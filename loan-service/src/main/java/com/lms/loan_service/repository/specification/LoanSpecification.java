package com.lms.loan_service.repository.specification;

import com.lms.loan_service.dto.book.BookResponseDTO;
import com.lms.loan_service.dto.loan.LoanSearchDTO;
import com.lms.loan_service.dto.student.StudentResponseDTO;
import com.lms.loan_service.entity.Loan;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoanSpecification {

    public static Specification<Loan> getSpecification(LoanSearchDTO searchDTO, Map<Long,BookResponseDTO> booksMap,
                                                       Map<Long, StudentResponseDTO> studentsMap){

        return (r,q,cb)->{
            if(searchDTO==null){
                cb.conjunction();
            }

            List<Predicate> predicates=new ArrayList<>();

            if (!booksMap.isEmpty()){
                predicates.add(r.get("bookId").in(booksMap.keySet()));
            }
            if (!studentsMap.isEmpty()){
                predicates.add(r.get("studentId").in(studentsMap.keySet()));
            }
            if (searchDTO.getStudentId()!=null){
                predicates.add(cb.equal(r.get("studentId"),searchDTO.getStudentId()));
            }
            if (searchDTO.getBookId()!=null){
                predicates.add(cb.equal(r.get("bookId"),searchDTO.getBookId()));
            }
            if (searchDTO.getStatus() != null){
                predicates.add(cb.equal(r.get("status"),searchDTO.getStatus().toString()));
            }
            if (searchDTO.getIssueDate()!=null){
                predicates.add(cb.equal(r.get("issueDate"),searchDTO.getIssueDate()));
            }
            if (searchDTO.getReturnDate()!=null){
                predicates.add(cb.equal(r.get("returnDate"),searchDTO.getReturnDate()));
            }
            if (searchDTO.getDueDate()!=null){
                predicates.add(cb.equal(r.get("dueDate"),searchDTO.getDueDate()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
