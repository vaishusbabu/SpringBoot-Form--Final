package com.example.Hospital_Form.controller;

import com.example.Hospital_Form.dto.ForgotPasswordRequest;
import com.example.Hospital_Form.dto.LoginRequest;
import com.example.Hospital_Form.dto.ResetPasswordRequest;
import com.example.Hospital_Form.entity.UserForm;
import com.example.Hospital_Form.exception.GlobalExceptionHandler;
import com.example.Hospital_Form.services.UserFormService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200") // Allowing access from Angular app
public class UserFormController {

    @Autowired
    private UserFormService userFormService;

    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody UserForm userForm, BindingResult result) {
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            response.put("success", false);
            response.put("message", "Validation errors");
            response.put("errors", getValidationErrors(result)); // Add the field-specific errors
            return ResponseEntity.badRequest().body(response);
        }

        try {
            UserForm savedUser = userFormService.registerUser(userForm);
            response.put("success", true);
            response.put("message", "Registration successful");
            response.put("data", savedUser); // Optionally return the saved user
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", "An error occurred during registration");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Helper method to extract validation errors
    private Map<String, String> getValidationErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getValidationErrors(result));
        }

        try {
            Optional<UserForm> userOptional = userFormService.loginUser(
                    loginRequest.getEmail(),
                    loginRequest.getPassword(),
                    loginRequest.getHealthCareNumber()
            );

            if (userOptional.isPresent()) {
                UserForm user = userOptional.get();

                // Structure the response
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login Successful");
                response.put("patientFirstName", user.getPatientFirstName());
                response.put("patientLastName", user.getPatientLastName());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid credentials");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest, BindingResult result) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Validate request body
        if (result.hasErrors()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value()); // Bad Request
            errorResponse.put("error", "Invalid forgot password request");
            errorResponse.put("message", result.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage()).collect(Collectors.toList())); // Collecting all error messages
            return ResponseEntity.badRequest()
                    .headers(headers)
                    .body(errorResponse);
        }

        String email = forgotPasswordRequest.getEmail();
        String healthCareNumber = forgotPasswordRequest.getHealthCareNumber();

        try {
            String message = userFormService.forgotPassword(email, healthCareNumber);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", HttpStatus.OK.value()); // OK
            responseBody.put("message", message); // E.g., "OTP sent to your email"
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(responseBody);
        } catch (GlobalExceptionHandler.ResourceNotFoundException e) {
            Map<String, Object> errorResponse = new HashMap<>();

            HttpStatus status;
            if (e.getMessage().equals("Email not found")) {
                status = HttpStatus.NOT_FOUND; // 404
            } else {
                status = HttpStatus.UNPROCESSABLE_ENTITY; // 422
            }

            errorResponse.put("status", status.value());
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(status) // Corrected to use HttpStatus directly
                    .headers(headers)
                    .body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value()); // Internal Server Error
            errorResponse.put("error", "An error occurred while sending OTP");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .headers(headers)
                    .body(errorResponse);
        }
    }

    // Reset password endpoint
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid reset password request");
        }
        try {
            String message = userFormService.resetPassword(
                    resetPasswordRequest.getEmail(),
                    resetPasswordRequest.getHealthCareNumber(),
                    resetPasswordRequest.getOtp(),
                    resetPasswordRequest.getNewPassword(),
                    resetPasswordRequest.getConfirmPassword()
            );
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
