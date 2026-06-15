package com.lms.student_service.repository;

import com.lms.student_service.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {

    Page<Student> findAll(Pageable pageable);

    List<Student> findByFirstName(String name);

    Optional<Student> findByEmail(String email);
}
