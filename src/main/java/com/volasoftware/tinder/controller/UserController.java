package com.volasoftware.tinder.controller;

import com.volasoftware.tinder.dto.LoginUserDto;
import com.volasoftware.tinder.dto.FullUserDto;
import com.volasoftware.tinder.dto.UserDto;
import com.volasoftware.tinder.jwt.JwtService;
import com.volasoftware.tinder.model.User;
import com.volasoftware.tinder.services.UserService;
import com.volasoftware.tinder.services.VerificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;
  private final VerificationService verificationService;
  private final JwtService jwtService;

  public UserController(
      UserService userService, VerificationService verificationService, JwtService jwtService) {
    this.userService = userService;
    this.verificationService = verificationService;
    this.jwtService = jwtService;
  }

  @GetMapping("/{id}")
  public User getUser(@PathVariable Long id) {
    return userService.getById(id).get();
  }

  @GetMapping("/profile")
  @SecurityRequirement(name = "bearer")
  public UserDto getUserProfile(){
    return userService.getUserProfile();
  }

  @PostMapping("/register")
  public ResponseEntity registerNewUser(@Valid @RequestBody FullUserDto fullUserDto)
      throws MessagingException, IOException {
    userService.registerUser(fullUserDto);

    return ResponseEntity.ok(HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity loginUser(@Valid @RequestBody LoginUserDto loginUserDto) {
    String jwtToken = jwtService.generateToken(userService.loginUser(loginUserDto));
    return ResponseEntity.ok(jwtToken);
  }

  @GetMapping("/verify/{token}")
  public ResponseEntity<?> verifyUser(@PathVariable String token) {
    return (verificationService.verifyUser(token))
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().build();
  }

  @PostMapping("/resend-verification-email")
  public ResponseEntity resendVerificationEmail(String email)
      throws MessagingException, IOException {
    verificationService.resendVerificationEmail(email);

    return ResponseEntity.ok(HttpStatus.OK);
  }

  @PutMapping("/profile")
  @SecurityRequirement(name = "bearer")
  public ResponseEntity editUser(@Valid @RequestBody FullUserDto fullUserDto)
      throws MessagingException, IOException {
    userService.editUser(fullUserDto);
    return ResponseEntity.ok(HttpStatus.OK);
  }

  @PostMapping("/forgotten-password")
  public ResponseEntity forgotPassword(String email)
          throws MessagingException, IOException{
    userService.generateNewPassword(email);
    return ResponseEntity.ok(HttpStatus.OK);
  }
}
