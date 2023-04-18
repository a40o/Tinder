package com.volasoftware.tinder.dto;

import com.volasoftware.tinder.model.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDto {

    @NotBlank(message = "No first name provided")
    private String firstName;

    @NotBlank(message = "No last name provided")
    private String lastName;

    @NotBlank(message = "No password provided")
    private String password;

    @NotBlank(message = "No email provided")
    @Email
    private String email;

    @NotBlank(message = "No gender provided")
    Gender gender;

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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}