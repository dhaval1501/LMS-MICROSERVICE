package com.lms.loan_service.mapper;

import com.lms.loan_service.dto.loan.LoanRequestDTO;
import com.lms.loan_service.dto.loan.LoanResponseDTO;
import com.lms.loan_service.entity.Loan;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanMapper {

    LoanResponseDTO toDto(Loan loan);

    Loan toEntity(LoanRequestDTO dto);

    List<LoanResponseDTO> toListDto(List<Loan> loans);
}
