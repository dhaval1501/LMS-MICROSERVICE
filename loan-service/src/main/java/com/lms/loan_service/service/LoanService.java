package com.lms.loan_service.service;

import com.lms.loan_service.dto.loan.LoanRequestDTO;
import com.lms.loan_service.dto.loan.LoanResponseDTO;
import com.lms.loan_service.dto.loan.LoanSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface LoanService {

    LoanResponseDTO createLoan(LoanRequestDTO requestDTO);

    LoanResponseDTO returnLoan(Long loanId);

    LoanResponseDTO getLoanById(Long loanId);


//    LoanResponseDTO renewLoan(Long loanId, LocalDate newDueDate);

    // List with pagination
    Page<LoanResponseDTO> getAllLoan(LoanSearchDTO searchDTO);

    Page<LoanResponseDTO> getLoansByStudent(LoanSearchDTO searchDTO);

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
