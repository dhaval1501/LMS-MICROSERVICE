package com.lms.loan_service.client;

import com.lms.loan_service.dto.student.StudentResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.List;
import java.util.Set;


@FeignClient(value = "student-service")
public interface StudentClient {

    @GetMapping("api/v1//students/byId/{id}")
    StudentResponseDTO getStudentById(@PathVariable Long id);

    @PostMapping("api/v1//students/bulk")
    List<StudentResponseDTO> getAllStudents(StudentResponseDTO searchDTO);

    @PostMapping("api/v1//students/by-ids")
    List<StudentResponseDTO> getStudentByIds(Set<Long> ids);


}
