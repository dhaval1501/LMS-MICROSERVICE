package com.lms.student_service.dto.student;

import lombok.Data;

@Data
public class StudentSearchDTO {
    private String firstName;
    private String lastName;
    private String number;
    private String email;
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "id";
    private String sortDirection = "DESC";
    private String city;
    private String pinCode;
}
