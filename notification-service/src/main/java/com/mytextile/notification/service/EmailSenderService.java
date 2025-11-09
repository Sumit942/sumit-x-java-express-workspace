package com.mytextile.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

// This is a MOCK service. In a real app, you would inject
// Spring's JavaMailSender and implement the logic.
@Service
@Slf4j
public class EmailSenderService {

    public void sendEmail(String to, String subject, String body) throws InterruptedException {
        log.info("Sending email to: {} with subject: {}", to, subject);
        
        // Simulate a slow network call
        Thread.sleep(2000); // 2-second delay
        
        // Simulate a potential failure
        if (to.contains("fail@example.com")) {
            throw new RuntimeException("Simulated email server failure");
        }

        log.info("Email successfully sent to: {}", to);
    }
}