package com.volasoftware.tinder;

import com.volasoftware.tinder.dto.FullUserDto;
import com.volasoftware.tinder.dto.UserDto;
import com.volasoftware.tinder.model.Gender;
import com.volasoftware.tinder.model.Role;
import com.volasoftware.tinder.model.User;
import com.volasoftware.tinder.model.Verification;
import com.volasoftware.tinder.repository.UserRepository;
import com.volasoftware.tinder.repository.VerificationRepository;
import com.volasoftware.tinder.services.EmailSenderService;
import com.volasoftware.tinder.services.UserService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserServiceTest {

  @Mock UserRepository userRepository;
  @Mock VerificationRepository verificationRepository;
  @Mock EmailSenderService emailSenderService;

  @Mock BCryptPasswordEncoder passwordEncoder;

  @InjectMocks UserService userService;

  @Test
  public void testRegistration() throws IOException, MessagingException {

    FullUserDto userDto = new FullUserDto();
    userDto.setEmail("test@test.com");
    userDto.setFirstName("Test");
    userDto.setLastName("Testov");
    userDto.setPassword(passwordEncoder.encode(("11Password!!")));
    userDto.setGender(Gender.MALE);

    User user = new User();
    user.setEmail("test@test.com");
    user.setFirstName("Test");
    user.setLastName("Testov");
    user.setPassword(passwordEncoder.encode("11Password!!"));
    user.setGender(Gender.MALE);
    user.setEnabled(false);
    user.setRole(Role.USER);

    Verification token = new Verification();
    token.setUser(user);
    token.setToken("token");
    token.setCreatedDate(LocalDateTime.now());
    token.setExpirationDate(LocalDateTime.now().plusDays(2));

    emailSenderService.sendEmail(
        "Verification", Collections.singleton(user.getEmail()), token.getToken());

    when(passwordEncoder.encode("11Password!!")).thenReturn("encoded");
    when(userRepository.findOneByEmail(userDto.getEmail())).thenReturn(Optional.empty());
    when(userRepository.saveAndFlush(any(User.class))).thenReturn(user);
    when(verificationRepository.saveAndFlush(any(Verification.class))).thenReturn(token);

    userService.registerUser(userDto);

    verify(userRepository, times(1)).saveAndFlush(any(User.class));
    verify(verificationRepository, times(1)).saveAndFlush(any(Verification.class));
    verify(emailSenderService, times(1))
        .sendEmail("Verification", Collections.singleton(user.getEmail()), "token");
  }

  @Test
  public void testEditUser() throws MessagingException, IOException {
    User user = new User();
    user.setEmail("test@test.com");
    user.setFirstName("Test");
    user.setLastName("Testov");
    user.setGender(Gender.FEMALE);

    FullUserDto input = new FullUserDto();
    input.setEmail("test@test.com");
    input.setFirstName("Tested");
    input.setLastName("User");
    input.setGender(Gender.MALE);

    when(userRepository.findOneByEmail(input.getEmail())).thenReturn(Optional.empty());
    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(user.getEmail());

    SecurityContext securityContext = mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);

    when(userRepository.findOneByEmail(authentication.getName())).thenReturn(Optional.of(user));
    when(userRepository.saveAndFlush(any(User.class))).thenReturn(user);


    userService.editUser(input);

    assertEquals(input.getEmail(), user.getEmail());
    assertEquals(input.getFirstName(), user.getFirstName());
    assertEquals(input.getLastName(), user.getLastName());
    assertEquals(input.getGender(), user.getGender());
    verify(userRepository, times(1)).saveAndFlush(any(User.class));

  }

  @Test
  public void testLoginUser() {
    User loggedUser = new User();
    loggedUser.setId(1L);
    loggedUser.setEmail("test@test.com");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(loggedUser.getEmail());

    SecurityContext securityContext = mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);

    when(userRepository.findOneByEmail(authentication.getName())).thenReturn(Optional.of(loggedUser));

    userService.getLoggedUser();

    verify(userRepository,times(1)).findOneByEmail(authentication.getName());
  }
}