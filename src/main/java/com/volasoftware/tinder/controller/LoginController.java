package com.volasoftware.tinder.controller;

import com.volasoftware.tinder.dto.LoginUserDto;
import com.volasoftware.tinder.jwt.JwtService;
import com.volasoftware.tinder.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final UserService userService;

    private final JwtService jwtService;

    public LoginController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/api/v1/users/login")
    public ResponseEntity loginUser(@Valid @RequestBody LoginUserDto loginUserDto) {
        String jwtToken = jwtService.generateToken(userService.loginUser(loginUserDto));
        return ResponseEntity.ok(jwtToken);
    }
}