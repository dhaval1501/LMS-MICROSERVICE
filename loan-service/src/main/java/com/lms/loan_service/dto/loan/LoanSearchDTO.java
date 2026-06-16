package com.lms.loan_service.dto.loan;

import com.lms.loan_service.dto.book.BookResponseDTO;
import com.lms.loan_service.dto.student.StudentResponseDTO;
import com.lms.loan_service.enums.LoanStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanSearchDTO {

    private StudentResponseDTO student;

    private BookResponseDTO book;

    private Long id;

    private Long studentId;

    private Long bookId;

    private LocalDate issueDate;

    private LocalDate dueDate;

    private LocalDate returnDate;

    private LoanStatus status;

    private Integer page=0;

    private Integer size= 10;

    private  String sortBy= "id";

    private String sortDirection = "DESC";

}
