package com.f3fitness.controller;

import com.f3fitness.model.RegistrationRequest;
import com.f3fitness.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) {
        try {
            logger.info("Received registration request from: {}", request.getEmail());
            emailService.sendRegistrationEmail(request);
            logger.info("Registration processed successfully for: {}", request.getEmail());
            return ResponseEntity.ok()
                .body(new RegistrationResponse(true, "Registration submitted successfully! We will contact you soon."));
        } catch (MessagingException e) {
            logger.error("Failed to process registration for {}: {}", request.getEmail(), e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(new RegistrationResponse(false, "Failed to submit registration. Please try again later."));
        } catch (Exception e) {
            logger.error("Unexpected error processing registration for {}: {}", request.getEmail(), e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(new RegistrationResponse(false, "An unexpected error occurred. Please try again later."));
        }
    }

    private record RegistrationResponse(boolean success, String message) {}
} 