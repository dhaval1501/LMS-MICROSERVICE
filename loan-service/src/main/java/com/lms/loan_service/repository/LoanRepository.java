package com.lms.loan_service.repository;

import com.lms.loan_service.entity.Loan;
import com.lms.loan_service.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByStudentId(Long studentId);

    boolean existsByStudentIdAndBookIdAndStatusIn(Long studentId,Long bookId,List<LoanStatus> statuses);

    List<Loan> findByBookId(Long bookId);

    List<Loan> findByStatus(LoanStatus status);
}
