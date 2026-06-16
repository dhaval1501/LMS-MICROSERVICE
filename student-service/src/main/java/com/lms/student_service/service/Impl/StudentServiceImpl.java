package com.lms.student_service.service.Impl;

import com.lms.student_service.dto.student.StudentRequestDTO;
import com.lms.student_service.dto.student.StudentResponseDTO;
import com.lms.student_service.dto.student.StudentSearchDTO;
import com.lms.student_service.dto.student.StudentUpdateDTO;
import com.lms.student_service.entity.Student;
import com.lms.student_service.mapper.StudentMapper;
import com.lms.student_service.repository.StudentRepository;
import com.lms.student_service.repository.specification.StudentSpecification;
import com.lms.student_service.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    @Transactional
    public StudentResponseDTO add(StudentRequestDTO requestDTO) {
        Student student= studentMapper.toEntity(requestDTO);
        student.getAddress().setStudent(student);
        StudentResponseDTO responseDTO=studentMapper.toDto(studentRepository.save(student));
        return responseDTO;
    }

    @Override
    public Page<StudentResponseDTO> getAllPageableStudents(StudentSearchDTO searchDTO) {

        int page = searchDTO.getPage() != null ? searchDTO.getPage() : 0;
        int size = searchDTO.getSize() != null ? searchDTO.getSize() : 10;

        String sortBy = StringUtils.hasText(searchDTO.getSortBy())
                ? searchDTO.getSortBy()
                : "id";

        Sort.Direction direction = Sort.Direction.DESC;

        if (StringUtils.hasText(searchDTO.getSortDirection())) {
            direction = Sort.Direction.fromString(
                    searchDTO.getSortDirection().toUpperCase()
            );
        }
        Sort sorting = Sort.by(
                Sort.Order.by(sortBy).with(direction)
        );

        Pageable pageable =  PageRequest.of(page,size,sorting);

        Page<Student> studentPage = studentRepository.findAll(StudentSpecification.getSpecification(searchDTO),pageable);
        return studentPage.map(studentMapper::toDto);

    }

    @Override
    public StudentResponseDTO update(Long id, StudentUpdateDTO updateDTO) {

        Student student =studentRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Student not found with id: "+id));
        student.setFirstName(updateDTO.getFirstName());
        student.setLastName(updateDTO.getLastName());
        student.setEmail(updateDTO.getEmail());
        student.setNumber(new BigInteger( updateDTO.getNumber()));
        if(updateDTO.getAddress() !=null){
            student.getAddress().setCity(updateDTO.getAddress().getCity());
            student.getAddress().setPinCode(updateDTO.getAddress().getPinCode());
        }
        Student updated = studentRepository.save(student);
        return studentMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        Student student =studentRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Student not found with id: "+id));
        studentRepository.delete(student);
    }

    @Override
    public StudentResponseDTO getById(Long id) {
        Student student =studentRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Student not found with id: "+id));
        return studentMapper.toDto(student);

    }

    @Override
    public List<StudentResponseDTO> getStudentsBySearch(StudentSearchDTO searchDTO) {
        return studentRepository
                .findAll(StudentSpecification.getSpecification(searchDTO))
                .stream()
                .map(studentMapper::toDto)
                .toList();
    }

    @Override
    public List<StudentResponseDTO> getStudentsByIds(Set<Long> ids) {
        return studentRepository
                .findByIdIn(ids)
                .stream()
                .map(studentMapper::toDto)
                .toList();
    }
    @Override
    public StudentResponseDTO getByEmail(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Student not found with email: "+email));
        return studentMapper.toDto(student);
    }

    @Override
    public List<StudentResponseDTO> getByName(String name) {

        List<Student> studentList = studentRepository.findByFirstName(name);
        return null;
    }


}
