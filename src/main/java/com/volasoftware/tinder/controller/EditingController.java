package com.volasoftware.tinder.controller;

import com.volasoftware.tinder.dto.UserDto;
import com.volasoftware.tinder.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class EditingController {
    private final UserService userService;

    public EditingController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/api/v1/users/profile")
    @SecurityRequirement(name = "bearer")
    public ResponseEntity editUser(@Valid @RequestBody UserDto userDto) throws MessagingException, IOException {
        userService.editUser(userDto);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
