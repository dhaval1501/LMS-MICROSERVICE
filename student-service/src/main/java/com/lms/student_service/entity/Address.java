package com.lms.student_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "address",
indexes = {
        @Index(name = "idx_address_city",columnList = "city"),
        @Index(name = "idx_address_pin_code",columnList = "pin_code")
}
)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "city",nullable = false,length = 50)
    private String city;

    @Column(name = "pin_code",nullable = false,length = 6)
    private Integer pinCode;

    @OneToOne(mappedBy ="address")
    private Student student;

    @CreationTimestamp
    @Column(updatable = false,name = "create_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;
}
