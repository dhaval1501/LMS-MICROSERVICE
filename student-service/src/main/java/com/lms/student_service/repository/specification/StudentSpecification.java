package com.lms.student_service.repository.specification;

import com.lms.student_service.dto.student.StudentSearchDTO;
import com.lms.student_service.entity.Address;
import com.lms.student_service.entity.Student;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class StudentSpecification {

    public static Specification<Student> getSpecification(StudentSearchDTO searchDTO) {

        return (r,q,cb)->{
            List<Predicate> predicates=new ArrayList<>();

            if(searchDTO== null){
                cb.conjunction();
            }
            if (StringUtils.hasText(searchDTO.getFirstName())){
                predicates.add(
                        cb.like(
                                cb.lower(r.get("firstName")),
                                "%"+searchDTO.getFirstName().toLowerCase()+"%"
                        )
                );
            }
            if (StringUtils.hasText(searchDTO.getLastName())){
                predicates.add(cb.like(cb.lower(r.get("lastName")),"%"+searchDTO.getLastName().toLowerCase()+"%"));
            }
            if (StringUtils.hasText(searchDTO.getNumber())){
                predicates.add(cb.like(cb.lower(r.get("number")),"%"+searchDTO.getNumber().toLowerCase()+"%"));
            }
            if (StringUtils.hasText(searchDTO.getEmail())){
                predicates.add(cb.like(cb.lower(r.get("email")),"%"+searchDTO.getEmail().toLowerCase()+"%"));
            }

            Join<Student, Address> addressJoin = r.join("address", JoinType.INNER);

            if (StringUtils.hasText(searchDTO.getCity())) {
                predicates.add(
                        cb.like(
                                cb.lower(addressJoin.get("city")),
                                "%" + searchDTO.getCity().toLowerCase() + "%"
                        )
                );
            }

            if (StringUtils.hasText(searchDTO.getPinCode())) {
                predicates.add(
                        cb.like(
                                cb.lower(addressJoin.get("pinCode")),
                                "%" + searchDTO.getPinCode()+ "%"
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));

        };

    }
}
