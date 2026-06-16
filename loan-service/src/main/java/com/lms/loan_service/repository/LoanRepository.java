package com.lms.loan_service.repository;

import com.lms.loan_service.entity.Loan;
import com.lms.loan_service.enums.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> , JpaSpecificationExecutor<Loan> {

    Page<Loan> findByStudentId(Long studentId, Pageable pageable);

    boolean existsByStudentIdAndBookIdAndStatusIn(Long studentId,Long bookId,List<LoanStatus> statuses);

    List<Loan> findByBookId(Long bookId);

    List<Loan> findByStatus(LoanStatus status);

    Page<Loan> findAll(Specification specification,Pageable pageable);
}
