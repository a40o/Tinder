package com.volasoftware.tinder.controller;

import com.volasoftware.tinder.services.VerificationService;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class VerificationController {

    private final VerificationService verificationService;


    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
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