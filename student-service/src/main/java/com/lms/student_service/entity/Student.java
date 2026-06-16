package com.lms.student_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "t_student",
        indexes = {
        @Index( name = "idx_student_first_name",columnList ="first_name" ),
        @Index( name = "idx_student_last_name",columnList ="last_name" ),
        @Index( name = "idx_student_number",columnList ="number" ),
        @Index( name = "idx_student_email",columnList ="email" )
        }
)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name",nullable = false,length = 50)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "number")
    private BigInteger number;

    @Column(name = "email",length = 100,unique = true)
    private String email;

    @CreationTimestamp
    @Column(updatable = false,name = "create_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;
}