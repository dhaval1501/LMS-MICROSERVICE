package com.lms.student_service.mapper;

import com.lms.student_service.dto.student.StudentRequestDTO;
import com.lms.student_service.dto.student.StudentResponseDTO;
import com.lms.student_service.dto.student.StudentUpdateDTO;
import com.lms.student_service.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface StudentMapper {

    @Mapping(source = "address",target = "address")
    Student toEntity(StudentRequestDTO requestDTO);

    @Mapping(source = "address", target = "address")
    StudentResponseDTO toDto(Student student);

    List<StudentResponseDTO> toDtoList(List<Student> studentList);

    @Mapping(source = "address",target = "address")
    Student toEntityUP(StudentUpdateDTO updateDTO);



}
