package com.lms.loan_service.dto.student;

import lombok.Data;

@Data
public class StudentResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String number;
    private String email;
    private String city;
    private String pinCode;
}