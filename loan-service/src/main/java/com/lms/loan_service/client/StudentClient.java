package com.lms.loan_service.client;

import com.lms.loan_service.dto.student.StudentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(value = "student-service")
public interface StudentClient {

    @GetMapping("api/v1//students/byId/{id}")
    StudentResponseDTO getStudentById(@PathVariable Long id);
}
