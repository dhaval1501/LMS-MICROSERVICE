package com.lms.student_service.repository;

import com.lms.student_service.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StudentRepository extends JpaRepository<Student,Long> , JpaSpecificationExecutor<Student> {

    @EntityGraph(attributePaths = "address")
    Page<Student> findAll(Specification<Student> studentSpecification,Pageable pageable);

    List<Student> findByFirstName(String name);

    Optional<Student> findByEmail(String email);

    List<Student> findByIdIn(Set<Long> ids);
}
