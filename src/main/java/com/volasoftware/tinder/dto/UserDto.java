package com.volasoftware.tinder.dto;

import com.volasoftware.tinder.model.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDto {

  @NotBlank(message = "No first name provided")
  @Pattern(regexp = "^[A-Za-z]*$", message = "Must contain only letters")
  @Size(min = 2, max = 50, message = "The size must be between 2 and 50 letters")
  private String firstName;

  @NotBlank(message = "No last name provided")
  @Pattern(regexp = "^[A-Za-z]*$", message = "Must contain only letters")
  @Size(min = 2, max = 50, message = "The size must be between 2 and 50 letters")
  private String lastName;

  @NotBlank(message = "No email provided")
  @Email
  private String email;

  private Gender gender;

  public UserDto(String firstName, String lastName, String email, Gender gender) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.gender = gender;
  }

  public UserDto() {
  }

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
