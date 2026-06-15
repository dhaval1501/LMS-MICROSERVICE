package com.lms.student_service.dto.student;

import com.lms.student_service.dto.address.AddressUpdateDTO;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StudentUpdateDTO {
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be 2–50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name must be max 50 characters")
    private String lastName;


    private AddressUpdateDTO address;

    @NotNull(message = "Mobile number is required")
    @Pattern(
            regexp = "^[0-9]{10,15}$",
            message = "Number must be 10 to 15 digits"
    )
    private String number;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100)
    private String email;
}
