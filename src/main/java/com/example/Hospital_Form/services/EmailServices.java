package com.example.Hospital_Form.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;  // Correct import for MessagingException (jakarta)
import jakarta.mail.internet.MimeMessage;  // Consistent jakarta package

@Service
public class EmailServices {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(toEmail);
        helper.setSubject("Your OTP for Password Reset");
        helper.setText("Your OTP is: " + otp + "\n" +
                "Reset Password Link: http://localhost:4200/resetpassword");


        mailSender.send(mimeMessage);
    }
}
