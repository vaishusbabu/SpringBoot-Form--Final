package com.example.Hospital_Form.services;

import com.example.Hospital_Form.entity.UserForm;
import com.example.Hospital_Form.repository.UserFormRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserFormService {

    @Autowired
    private UserFormRepository userFormRepository;

    @Autowired
    private EmailServices emailService;

    // Method to register a new user
    @Transactional
    public UserForm registerUser(UserForm userForm) {
        // Check if the health care number already exists
        Optional<UserForm> existingHealthCareNumber = userFormRepository.findByHealthCareNumber(userForm.getHealthCareNumber());
        if (existingHealthCareNumber.isPresent()) {
            throw new IllegalStateException("Health care number already in use");
        }

        // Allow duplicate emails
        // You can still check if the user already exists by the email if you need to,
        // but we will allow it for the sake of this requirement.

        try {
            // Save the new user with the validated health care number
            System.out.println("Saving user: " + userForm); // Debugging log
            return userFormRepository.save(userForm);
        } catch (Exception e) {
            // Log the exception for debugging purposes
            System.err.println("Error occurred during user registration: " + e.getMessage());
            throw new RuntimeException("An error occurred during registration: " + e.getMessage(), e);
        }
    }

    // Method to log in a user
    public Optional<UserForm> loginUser(String email, String password, String healthCareNumber) {
        Optional<UserForm> user = userFormRepository.findByEmailAndHealthCareNumber(email, healthCareNumber);

        // Check for user authentication
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user; // User authenticated
        }

        return Optional.empty(); // Return empty if authentication fails
    }

    // Method to handle forgot password
    public String forgotPassword(String email, String healthCareNumber) {
        Optional<UserForm> userOptional = userFormRepository.findByEmailAndHealthCareNumber(email, healthCareNumber);

        if (userOptional.isPresent()) {
            UserForm user = userOptional.get();

            // Generate OTP and set expiration time (15 minutes from now)
            String otp = generateOtp();
            user.setOtp(otp);
            user.setOtpExpiration(LocalDateTime.now().plusMinutes(15));

            // Save user with OTP details
            userFormRepository.save(user);

            // Send OTP via email
            try {
                emailService.sendOtpEmail(email, otp);
            } catch (MessagingException e) {
                throw new RuntimeException("Failed to send OTP email", e);
            }

            return "OTP sent to your email";
        } else {
            throw new IllegalStateException("User not found with provided email and healthcare number");
        }
    }

    // Method to reset password
    public String resetPassword(String email, String healthCareNumber, String otp, String newPassword, String confirmPassword) {
        Optional<UserForm> userOptional = userFormRepository.findByEmailAndHealthCareNumber(email, healthCareNumber);

        if (userOptional.isPresent()) {
            UserForm user = userOptional.get();

            // Validate OTP and check if it hasn't expired
            if (user.getOtp() != null && user.getOtp().equals(otp) && user.getOtpExpiration().isAfter(LocalDateTime.now())) {
                // Check if passwords match
                if (newPassword.equals(confirmPassword)) {
                    // Update the password and clear OTP
                    user.setPassword(newPassword);
                    user.setOtp(null);
                    user.setOtpExpiration(null);

                    // Save the updated user
                    userFormRepository.save(user);

                    return "Password successfully reset";
                } else {
                    throw new IllegalStateException("Passwords do not match");
                }
            } else {
                throw new IllegalStateException("Invalid or expired OTP");
            }
        } else {
            throw new IllegalStateException("User not found with provided email and healthcare number");
        }
    }

    // Helper method to generate a random 14-digit health care number
    private String generateHealthCareNumber() {
        Random random = new Random();
        long number = (long) (random.nextDouble() * 9_000_000_000_000L) + 1_000_000_000_000L; // Generates a 14-digit number
        return String.valueOf(number);
    }

    // Helper method to generate a random 6-digit OTP
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generates a random 6-digit number
        return String.valueOf(otp);
    }
}
