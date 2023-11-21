package com.volasoftware.tinder.dto;

import com.volasoftware.tinder.annotation.Password;
import com.volasoftware.tinder.model.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class UserDto {

    @NotBlank(message = "No first name provided")
    @Pattern(regexp="^[A-Za-z]*$",message = "Must contain only letters")
    @Size(min = 2, max = 50, message = "The size must be between 2 and 50 letters")
    private String firstName;

    @NotBlank(message = "No last name provided")
    @Pattern(regexp="^[A-Za-z]*$",message = "Must contain only letters")
    @Size(min = 2, max = 50, message = "The size must be between 2 and 50 letters")
    private String lastName;

    @NotBlank(message = "No password provided")
    @Password
    private String password;

    @NotBlank(message = "No email provided")
    @Email
    private String email;

    @NotBlank(message = "No gender provided")
    private String gender;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}