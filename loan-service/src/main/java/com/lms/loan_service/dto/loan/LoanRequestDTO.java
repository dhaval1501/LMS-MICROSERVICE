package com.lms.loan_service.dto.loan;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanRequestDTO {

    @NotNull
    private Long studentId;

    @NotNull
    private Long bookId;

    private LocalDate dueDate;
}
