package com.lms.student_service.repository;

import com.lms.student_service.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {

    Optional<UserInfo> findUserInfoByEmail(String email);
}
