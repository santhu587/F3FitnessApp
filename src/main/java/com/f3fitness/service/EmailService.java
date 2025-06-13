package com.f3fitness.service;

import com.f3fitness.model.RegistrationRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendRegistrationEmail(RegistrationRequest request) throws MessagingException {
        try {
            logger.debug("Preparing to send registration email for: {}", request.getEmail());
            
            Context context = new Context();
            context.setVariable("registration", request);

            String emailContent = templateEngine.process("registration-email", context);
            logger.debug("Email template processed successfully");

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom("${spring.mail.username}");
            helper.setTo("santhoshckm2001@gmail.com");
            helper.setSubject("New Gym Registration");
            helper.setText(emailContent, true);

            logger.debug("Attempting to send email...");
            emailSender.send(message);
            logger.info("Registration email sent successfully to admin");
        } catch (Exception e) {
            logger.error("Failed to send registration email: {}", e.getMessage(), e);
            throw new MessagingException("Failed to send registration email", e);
        }
    }
} 