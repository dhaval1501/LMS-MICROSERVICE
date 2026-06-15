package com.lms.student_service.dto.student;

import com.lms.student_service.dto.address.AddressResponseDTO;
import lombok.Data;

@Data
public class StudentResponseDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private AddressResponseDTO address;

    private String number;

    private String email;
}
