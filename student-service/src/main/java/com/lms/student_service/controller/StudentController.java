package com.lms.student_service.controller;

import com.lms.student_service.dto.student.StudentRequestDTO;
import com.lms.student_service.dto.student.StudentResponseDTO;
import com.lms.student_service.dto.student.StudentSearchDTO;
import com.lms.student_service.dto.student.StudentUpdateDTO;
import com.lms.student_service.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


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

    @PostMapping("/all")
    @Operation(summary = "Get all Pageable students with searching and sorting")
    public ResponseEntity<Page<StudentResponseDTO>>getAllStudent(
            @RequestBody  StudentSearchDTO searchDTO
    ){

        Page<StudentResponseDTO> responseDTOPage=studentService.getAllPageableStudents(searchDTO);
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

    @PostMapping("/bulk")
    @Operation(summary = "Get All Students ")
    public ResponseEntity<List<StudentResponseDTO>> getAllBooks(@RequestBody StudentSearchDTO searchDTO) {
        List<StudentResponseDTO> responseDTOList=studentService.getStudentsBySearch(searchDTO);
        return new ResponseEntity<>(responseDTOList,HttpStatus.OK);
    }

    @PostMapping("/by-ids")
    @Operation(summary = "Get All Books By Ids ")
    public ResponseEntity<List<StudentResponseDTO>> getAllBooks(@RequestBody Set<Long> ids) {
        List<StudentResponseDTO> responseDTOList=studentService.getStudentsByIds(ids);
        return new ResponseEntity<>(responseDTOList,HttpStatus.OK);
    }

//    @GetMapping("/byName/{name}")
//    public List<Student> getStudentByName(@PathVariable String name){
//
//        List<Student> studentList= studentService.getByName(name);
//        return studentList;
//    }

}
