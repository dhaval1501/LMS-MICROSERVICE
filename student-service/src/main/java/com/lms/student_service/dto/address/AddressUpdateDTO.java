package com.lms.student_service.dto.address;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AddressUpdateDTO {
    private Long id;
    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City must not exceed 50 characters")
    private String city;
    @NotNull(message = "Pin code is required")
    @Min(value = 100000, message = "Pin code must be 6 digits")
    @Max(value = 999999, message = "Pin code must be 6 digits")
    private Integer pinCode;

}
