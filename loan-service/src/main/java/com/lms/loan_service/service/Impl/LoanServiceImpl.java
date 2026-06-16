package com.lms.loan_service.service.Impl;

import com.lms.loan_service.dto.book.BookResponseDTO;
import com.lms.loan_service.dto.loan.LoanRequestDTO;
import com.lms.loan_service.dto.loan.LoanResponseDTO;
import com.lms.loan_service.dto.loan.LoanSearchDTO;
import com.lms.loan_service.dto.student.StudentResponseDTO;
import com.lms.loan_service.entity.Loan;
import com.lms.loan_service.enums.LoanStatus;
import com.lms.loan_service.exception.BookUnavailableException;
import com.lms.loan_service.exception.DuplicateResourceException;
import com.lms.loan_service.exception.ResourceNotFoundException;
import com.lms.loan_service.exception.ServiceUnavailableException;
import com.lms.loan_service.mapper.LoanMapper;
import com.lms.loan_service.repository.LoanRepository;
import com.lms.loan_service.client.BookClient;
import com.lms.loan_service.repository.specification.LoanSpecification;
import com.lms.loan_service.service.LoanService;
import com.lms.loan_service.client.StudentClient;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final BookClient bookClient;
    private final StudentClient studentClient;

    private StudentResponseDTO getStudentByClient(Long studentId){
        try {
            return studentClient.getStudentById(studentId);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException(
                    "Student not found with id: " + studentId);
        } catch (FeignException e) {
            throw new ServiceUnavailableException("Student service unavailable while getting: " + e.getMessage() +" status " +e.status());
        }
    }

    private BookResponseDTO getBookByClient(Long bookId){
        try {
           return bookClient.getBookById(bookId);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException(
                    "Book not found with id: " + bookId);
        } catch (FeignException e) {
            throw new ServiceUnavailableException("Book service unavailable while getting: " + e.getMessage() +" status " +e.status());
        }
    }

    private Map<Long,BookResponseDTO> getAllBooksByFilter(BookResponseDTO searchDTO){
        try {
            if(searchDTO == null){return  Collections.emptyMap();}
           return bookClient.getAllBooks(searchDTO)
                   .stream()
                   .collect(Collectors.toMap(BookResponseDTO::getId, Function.identity()));
        }

        catch (FeignException e) {
            throw new ServiceUnavailableException("Book service unavailable while getting: " + e.getMessage() +" status " +e.status());
        }
    }

    private Map<Long,StudentResponseDTO> getAllStudentByFilter(StudentResponseDTO searchDTO){
        try {
            if(searchDTO == null){return  Collections.emptyMap();}
           return studentClient.getAllStudents(searchDTO)
                   .stream()
                   .collect(Collectors.toMap(StudentResponseDTO::getId, Function.identity()));
        }

        catch (FeignException e) {
            throw new ServiceUnavailableException("Student service unavailable while getting: " + e.getMessage() +" status " +e.status());
        }
    }

    private List<BookResponseDTO> getAllBooksByIds(Set<Long> ids){
        try {
           return bookClient.getBookByIds(ids);
        }
        catch (FeignException e) {
            throw new ServiceUnavailableException("Book service unavailable while getting: " + e.getMessage() +" status " +e.status());
        }
    }

    private List<StudentResponseDTO> getAllStudentsByIds(Set<Long> ids){
        try {
           return studentClient.getStudentByIds(ids);
        }
        catch (FeignException e) {
            throw new ServiceUnavailableException("Student service unavailable while getting: " + e.getMessage() +" status " +e.status());
        }
    }

    private BookResponseDTO issueBookByClient(Long bookId){
        try {
           return bookClient.issueBook(bookId);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException(
                    "Book not found with id: " + bookId);
        } catch (FeignException e) {
            throw new ServiceUnavailableException("Book service unavailable while issue: " + e.getMessage() +" status " +e.status());
        }
    }

    private BookResponseDTO returnBookByClient(Long bookId){
        try {
           return bookClient.returnBook(bookId);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException(
                    "Book not found with id: " + bookId);
        } catch (FeignException e) {
            throw new ServiceUnavailableException("Book service unavailable return: " + e.getMessage() +" status " +e.status());
        }
    }


    @Override
    public LoanResponseDTO createLoan(LoanRequestDTO requestDTO) {

        // Book already issued by student
        if (loanRepository.existsByStudentIdAndBookIdAndStatusIn(
                requestDTO.getStudentId(),
                requestDTO.getBookId(),
                List.of(LoanStatus.ISSUED, LoanStatus.OVERDUE))
        ) throw new DuplicateResourceException("Book already issued can't issued again");

        //get student using student-service exception handle by Feign Error Decoder
        StudentResponseDTO studentResponseDTO = getStudentByClient(requestDTO.getStudentId());

        //get book using book-service exception handle by Feign Error Decoder
        BookResponseDTO bookResponseDTO = getBookByClient(requestDTO.getBookId());

        //check book availability
        if (bookResponseDTO.getAvailableCopies() <= 0) {
            throw new BookUnavailableException("No copies available for book: " + bookResponseDTO.getTitle());
        }

        //updating book availability
        bookResponseDTO=issueBookByClient(bookResponseDTO.getId());

        try{
            Loan loan = loanMapper.toEntity(requestDTO);
            loan.setIssueDate(LocalDate.now());
            loan.setDueDate(requestDTO.getDueDate() != null ? requestDTO.getDueDate() : LocalDate.now().plusDays(7));
            loan.setStatus(LoanStatus.ISSUED);

            //save loan entry
            LoanResponseDTO loanResponseDTO = loanMapper.toDto(loanRepository.save(loan));

            loanResponseDTO.setBook(bookResponseDTO);
            loanResponseDTO.setStudent(studentResponseDTO);
            return loanResponseDTO;
        }catch (Exception ex) {

            // COMPENSATION (ROLLBACK)
            returnBookByClient(requestDTO.getBookId());

            throw new RuntimeException("Saga failed, rolled back");
        }
    }

    @Override
    public LoanResponseDTO returnLoan(Long loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Record not found: " + loanId));
        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new RuntimeException("Book is already returned. No further action is allowed.");
        }

        loan.setReturnDate(LocalDate.now());
        loan.setStatus(LoanStatus.RETURNED);
        LoanResponseDTO responseDTO=loanMapper.toDto(loanRepository.save(loan));

        returnBookByClient(loan.getBookId());

        return responseDTO;
    }

    @Override
    public LoanResponseDTO getLoanById(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Record not found: " + loanId));

        StudentResponseDTO studentResponseDTO = getStudentByClient(loan.getStudentId());

        BookResponseDTO bookResponseDTO= getBookByClient(loan.getBookId());

        LoanResponseDTO responseDTO = loanMapper.toDto(loan);
        responseDTO.setStudent(studentResponseDTO);
        responseDTO.setBook(bookResponseDTO);
        return responseDTO;
    }

    @Override
    public Page<LoanResponseDTO> getAllLoan(LoanSearchDTO searchDTO) {

        Pageable pageable = setPageable(searchDTO);

        //fetch filtered data (returns empty map if no filter)
        Map<Long, BookResponseDTO> booksMap = getAllBooksByFilter(searchDTO.getBook());
        Map<Long, StudentResponseDTO> studentsMap = getAllStudentByFilter(searchDTO.getStudent());

        //fetch loans using specification
        Page<Loan> loanPage = loanRepository.findAll(
                LoanSpecification.getSpecification(searchDTO, booksMap,studentsMap),
                pageable
        );

        if (loanPage.isEmpty()) {
            return Page.empty(pageable);
        }

        // batch fetch only if filter was NOT applied
        if (searchDTO.getBook() == null) {
            Set<Long> bookIds = loanPage.map(Loan::getBookId).toSet();
            booksMap = getAllBooksByIds(bookIds)
                    .stream()
                    .collect(Collectors.toMap(BookResponseDTO::getId, Function.identity()));
        }

        if (searchDTO.getStudent() == null) {
            Set<Long> studentIds = loanPage.map(Loan::getStudentId).toSet();
            studentsMap = getAllStudentsByIds(studentIds)
                    .stream()
                    .collect(Collectors.toMap(StudentResponseDTO::getId, Function.identity()));
        }

        final Map<Long, BookResponseDTO> finalBookMap = booksMap;
        final Map<Long, StudentResponseDTO> finalStudentMap = studentsMap;

        return loanPage.map(loan -> buildLoanResponse(loan, finalBookMap, finalStudentMap));
    }

    private LoanResponseDTO buildLoanResponse(
            Loan loan,
            Map<Long, BookResponseDTO> booksMap,
            Map<Long, StudentResponseDTO> studentsMap) {

        LoanResponseDTO dto = loanMapper.toDto(loan);
        dto.setBook(booksMap.getOrDefault(loan.getBookId(), null));
        dto.setStudent(studentsMap.getOrDefault(loan.getStudentId(), null));
        return dto;
    }

    private  Pageable setPageable(LoanSearchDTO searchDTO){

        int page = searchDTO.getPage() != null ? searchDTO.getPage() : 0;
        int size = searchDTO.getSize() != null ? searchDTO.getSize() : 10;

        String sortBy= StringUtils.hasText(searchDTO.getSortBy()) ? searchDTO.getSortBy() : "id";

        Sort.Direction direction = Sort.Direction.DESC;

        if (StringUtils.hasText(searchDTO.getSortDirection())) {
            direction = Sort.Direction.fromString(
                    searchDTO.getSortDirection().toUpperCase()
            );
        }
        Sort sorting = Sort.by(
                Sort.Order.by(sortBy).with(direction)
        );

        return PageRequest.of(page,size,sorting);
    }

    @Override
    public Page<LoanResponseDTO> getLoansByStudent(LoanSearchDTO searchDTO) {
        Pageable pageable = setPageable(searchDTO);
        List<Long> booksId=new ArrayList<>();
        if(searchDTO.getBook()!=null){
            booksId=bookClient.getAllBooks(searchDTO.getBook()).stream().map(BookResponseDTO::getId).toList();
        }
        Page<Loan> loanList=loanRepository.findAll(LoanSpecification.getSpecification(searchDTO,null,null),pageable);
        return loanList.map(loan->{
            LoanResponseDTO responseDTO=loanMapper.toDto(loan);
            responseDTO.setBook(getBookByClient(loan.getBookId()));
//            responseDTO.setStudent();
            return responseDTO;
        });
    }
}
