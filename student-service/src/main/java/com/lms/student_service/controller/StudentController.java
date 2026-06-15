package com.lms.student_service.controller;

import com.lms.student_service.dto.student.StudentRequestDTO;
import com.lms.student_service.dto.student.StudentResponseDTO;
import com.lms.student_service.dto.student.StudentUpdateDTO;
import com.lms.student_service.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequiredArgsConstructor
@Tag(name = "Student Controller", description = "APIs for Student management")
//@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;


    @PostMapping("/add")
    @Operation(summary = "Add new student")
    public ResponseEntity<StudentResponseDTO> addStudent(@Valid @RequestBody StudentRequestDTO requestDTO){
        StudentResponseDTO responseDTO=studentService.add(requestDTO);
       return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all Pageable students")
    public ResponseEntity<Page<StudentResponseDTO>>getAllStudent(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort
    ){
        Sort sorting = Sort.by(
                Sort.Order.by(sort[0]).with(Sort.Direction.fromString(sort[1]))
        );

        Pageable pageable =  PageRequest.of(page, size,sorting);
        Page<StudentResponseDTO> responseDTOPage=studentService.getAll(pageable);
        return new ResponseEntity<>(responseDTOPage,HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update Student By Id")
    public ResponseEntity<StudentResponseDTO> updateStudent(@PathVariable Long id,@Valid @RequestBody StudentUpdateDTO updateDTO){
        StudentResponseDTO responseDTO = studentService.update(id,updateDTO);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete student By Id")
    public ResponseEntity<?>  deleteStudent(@PathVariable Long id){
        studentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/byId/{id}")
    @Operation(summary = "Get student By Id")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id){

        StudentResponseDTO responseDTO =  studentService.getById(id);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

//    @GetMapping("/byName/{name}")
//    public List<Student> getStudentByName(@PathVariable String name){
//
//        List<Student> studentList= studentService.getByName(name);
//        return studentList;
//    }

}
