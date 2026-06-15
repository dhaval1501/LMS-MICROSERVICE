package com.lms.service;

import com.lms.student_service.dto.user.UserRequestDTO;
import com.lms.student_service.dto.user.UserResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
}
