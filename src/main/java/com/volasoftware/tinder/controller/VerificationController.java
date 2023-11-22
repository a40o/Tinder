package com.volasoftware.tinder.controller;

import com.volasoftware.tinder.repository.UserRepository;
import com.volasoftware.tinder.repository.VerificationRepository;
import com.volasoftware.tinder.services.EmailSenderService;
import com.volasoftware.tinder.services.UserService;
import com.volasoftware.tinder.services.VerificationService;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
public class VerificationController {

    private final VerificationService verificationService;
    private final VerificationRepository verificationRepository;
    private final JavaMailSender mailSender;
    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;

    public VerificationController(VerificationService verificationService,
                                  VerificationRepository verificationRepository,
                                  JavaMailSender mailSender,
                                  UserService userService,
                                  UserRepository userRepository,
                                  EmailSenderService emailSenderService) {
        this.verificationService = verificationService;
        this.verificationRepository = verificationRepository;
        this.mailSender = mailSender;
        this.userService = userService;
        this.userRepository = userRepository;
        this.emailSenderService = emailSenderService;
    }
    
    @GetMapping("/verify/{token}")
    public ResponseEntity<?> verifyUser(@PathVariable String token){
        return (verificationService.verifyUser(token)) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PostMapping("api/v1/users/resend-verification-email")
    public ResponseEntity resendVerificationEmail(String email) throws MessagingException, IOException {
        verificationService.resendVerificationEmail(email);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}