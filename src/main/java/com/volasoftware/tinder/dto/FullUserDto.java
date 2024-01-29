package com.volasoftware.tinder.dto;

import com.volasoftware.tinder.annotation.Password;
import com.volasoftware.tinder.model.Gender;
import jakarta.validation.constraints.NotBlank;

public class FullUserDto extends UserDto {

  @NotBlank(message = "No password provided")
  @Password
  private String password;

  public FullUserDto(String firstName, String lastName, String email, Gender gender) {
    super(firstName, lastName, email, gender);
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
