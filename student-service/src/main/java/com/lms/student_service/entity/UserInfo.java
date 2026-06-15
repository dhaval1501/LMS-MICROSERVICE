package com.lms.student_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import com.lms.enums.Role;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 50)
    private String name;

    @Column(unique = true,nullable = false,length = 100)
    private String email;

    @Column(nullable = false,length = 50)
    private String password;

//    @Enumerated(EnumType.STRING)
//    private Role role;
}
