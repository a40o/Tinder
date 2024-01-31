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
import com.volasoftware.tinder.services.VerificationService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserServiceTest {

  @Mock UserRepository userRepository;
  @Mock VerificationRepository verificationRepository;
  @Mock EmailSenderService emailSenderService;
  @Mock VerificationService verificationService;

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
    // doNothing().when(emailSenderService).sendEmail(any(), anySet(), any());

    userService.registerUser(userDto);

    verify(userRepository, times(1)).saveAndFlush(any(User.class));
    verify(verificationRepository, times(1)).saveAndFlush(any(Verification.class));
    verify(emailSenderService, times(1))
        .sendEmail("Verification", Collections.singleton(user.getEmail()), "token");
  }
}
