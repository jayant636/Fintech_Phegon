package com.example.phegon.phegonBank.auth_user.repository;

import com.example.phegon.phegonBank.auth_user.entity.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordResetCode,Long> {
    Optional<PasswordResetCode> findByCode(String code);
    void deleteByUserId(Long userId);
}
