package com.project.dailyincome;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String email, String otp) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Budget Tracker Password Reset OTP");

        message.setText(
                "Your OTP is: " + otp +
                "\n\nDo not share it with anyone.");

        mailSender.send(message);
    }
    public void sendLoginAlert(String email,
            String username) {

SimpleMailMessage message =
new SimpleMailMessage();

message.setTo(email);

message.setSubject(
"Login Alert - Budget Tracker");

message.setText(
"Hello " + username +
",\n\nYour Budget Tracker account "
+ "was logged in successfully."
+ "\n\nIf this was not you, "
+ "please reset your password.");

mailSender.send(message);
}
}