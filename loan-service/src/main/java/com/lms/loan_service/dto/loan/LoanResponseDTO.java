package com.lms.loan_service.dto.loan;

import com.lms.loan_service.dto.book.BookResponseDTO;
import com.lms.loan_service.dto.student.StudentResponseDTO;
import com.lms.loan_service.enums.LoanStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LoanResponseDTO {

    private Long id;

    private StudentResponseDTO student;

    private BookResponseDTO book;

    private LocalDate issueDate;

    private LocalDate dueDate;

    private LocalDate returnDate;

    private LoanStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
