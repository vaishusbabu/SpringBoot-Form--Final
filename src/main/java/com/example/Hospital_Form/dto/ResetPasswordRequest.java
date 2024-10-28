package com.example.Hospital_Form.dto;


import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String email;
    private String healthCareNumber;
    private String otp;
    private String newPassword;
    private String confirmPassword;
}
