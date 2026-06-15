package com.lms.loan_service.controller;

import com.lms.loan_service.dto.loan.LoanRequestDTO;
import com.lms.loan_service.dto.loan.LoanResponseDTO;
import com.lms.loan_service.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loan")
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/create")
    @Operation(summary = "Create issue book entry")
    public ResponseEntity<LoanResponseDTO> createLoan(@RequestBody @Valid LoanRequestDTO dto) {
        LoanResponseDTO responseDTO=loanService.createLoan(dto);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/return/{id}")
    @Operation(summary = "update book entry to return")
    public ResponseEntity<LoanResponseDTO> returnLoan(@PathVariable Long id) {
        LoanResponseDTO responseDTO=loanService.returnLoan(id);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "get by id")
    public ResponseEntity<LoanResponseDTO> getLoan(@PathVariable Long id) {
        LoanResponseDTO responseDTO=loanService.getLoanById(id);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/all")
    @Operation(summary = "get all entries")
    public ResponseEntity<Page<LoanResponseDTO>> geAllLoan(
            @RequestParam int page,
            @RequestParam int size
    ) {
        Pageable pageable=  PageRequest.of(page,size);
        Page<LoanResponseDTO> responseDTOList=loanService.getAllLoan(pageable);
        return new ResponseEntity<>(responseDTOList, HttpStatus.OK);
    }
}
