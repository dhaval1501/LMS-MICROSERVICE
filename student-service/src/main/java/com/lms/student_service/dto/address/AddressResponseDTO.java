package com.lms.student_service.dto.address;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AddressResponseDTO {
    private Long id;
    private String city;
    private Integer pinCode;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
