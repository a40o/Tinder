package com.volasoftware.tinder.services;

import com.volasoftware.tinder.dto.UserDto;
import com.volasoftware.tinder.exception.EmailAlreadyRegisteredException;
import com.volasoftware.tinder.model.Gender;
import com.volasoftware.tinder.model.User;
import com.volasoftware.tinder.model.Verification;
import com.volasoftware.tinder.repository.UserRepository;
import com.volasoftware.tinder.repository.VerificationRepository;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final VerificationRepository verificationRepository;
    private final EmailSenderService emailSenderService;
    private final VerificationService verificationService;
    private final Set<User> users = new HashSet<User>();

    public UserService(UserRepository userRepository,
                       VerificationRepository verificationRepository,
                       EmailSenderService emailSenderService,
                       VerificationService verificationService) {
        this.userRepository = userRepository;
        this.verificationRepository = verificationRepository;
        this.emailSenderService = emailSenderService;
        this.verificationService = verificationService;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void registerUser(UserDto userDto) throws MessagingException, IOException {

        if(userRepository.findOneByEmail(userDto.getEmail()).isPresent()) {
            throw new EmailAlreadyRegisteredException("This email is already registered!");
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setGender(Gender.valueOf(userDto.getGender()));
        user.setEnabled(false);
        userRepository.saveAndFlush(user);
        users.add(user);

        Verification token = new Verification();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setCreatedDate(LocalDateTime.now());
        token.setExpirationDate(LocalDateTime.now().plusDays(2));
        verificationRepository.saveAndFlush(token);

            emailSenderService.sendEmail("Verification",
                    Collections.singleton(user.getEmail()),
                    verificationService.setVerificationLink(String.valueOf(token)));
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }
}