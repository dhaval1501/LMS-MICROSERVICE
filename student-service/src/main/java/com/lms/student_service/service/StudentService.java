package com.lms.student_service.service;

import com.lms.student_service.dto.student.StudentRequestDTO;
import com.lms.student_service.dto.student.StudentResponseDTO;
import com.lms.student_service.dto.student.StudentSearchDTO;
import com.lms.student_service.dto.student.StudentUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface StudentService {
    StudentResponseDTO add(StudentRequestDTO requestDTO);

     Page<StudentResponseDTO> getAllPageableStudents(StudentSearchDTO searchDTO);

    StudentResponseDTO update (Long id, StudentUpdateDTO updateDTO);

    void delete (Long id);

    StudentResponseDTO getById(Long id);

    List<StudentResponseDTO> getStudentsByIds(Set<Long> ids);

    List<StudentResponseDTO> getStudentsBySearch(StudentSearchDTO searchDTO);

    StudentResponseDTO getByEmail(String email);

     List<StudentResponseDTO> getByName(String name);
}
