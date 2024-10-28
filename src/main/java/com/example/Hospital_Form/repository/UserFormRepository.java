package com.example.Hospital_Form.repository;

import com.example.Hospital_Form.entity.UserForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFormRepository extends JpaRepository<UserForm, String> {

    // Find a user by email
    Optional<UserForm> findByEmail(String email);

    // Find a user by health care number
    Optional<UserForm> findByHealthCareNumber(String healthCareNumber);

    // Find a user by both email and health care number (to ensure uniqueness during login)
    Optional<UserForm> findByEmailAndHealthCareNumber(String email, String healthCareNumber);
}
