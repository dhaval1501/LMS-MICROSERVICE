package com.lms.loan_service.service;

import com.lms.loan_service.dto.loan.LoanRequestDTO;
import com.lms.loan_service.dto.loan.LoanResponseDTO;
import com.lms.loan_service.enums.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface LoanService {

    LoanResponseDTO createLoan(LoanRequestDTO requestDTO);

    LoanResponseDTO returnLoan(Long loanId);

    LoanResponseDTO getLoanById(Long loanId);

//     List<LoanResponseDTO> getAllLoan();

//    LoanResponseDTO renewLoan(Long loanId, LocalDate newDueDate);

    // List with pagination
    Page<LoanResponseDTO> getAllLoan(Pageable pageable);

    // Filters
//    Page<LoanResponseDTO> getLoansByStatus(LoanStatus status, int page, int size);
//    List<LoanResponseDTO> getLoansByStudentId(Long studentId);
//    List<LoanResponseDTO> getLoansByBookId(Long bookId);

    // Student history

    // Overdue
//    void updateOverdueLoans();        // called by scheduler

    // Stats
//    LoanStatsDTO getLoanStats();


}
