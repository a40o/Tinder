package com.volasoftware.tinder.services;

import com.volasoftware.tinder.dto.LoginUserDto;
import com.volasoftware.tinder.dto.FullUserDto;
import com.volasoftware.tinder.dto.UserDto;
import com.volasoftware.tinder.exception.*;
import com.volasoftware.tinder.model.Role;
import com.volasoftware.tinder.model.User;
import com.volasoftware.tinder.model.Verification;
import com.volasoftware.tinder.repository.UserRepository;
import com.volasoftware.tinder.repository.VerificationRepository;
import jakarta.mail.MessagingException;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    @Bean
    private final BCryptPasswordEncoder encodePassword(){
        return new BCryptPasswordEncoder();
    };

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

    public void registerUser(FullUserDto fullUserDto) throws MessagingException, IOException {

        if(userRepository.findOneByEmail(fullUserDto.getEmail()).isPresent()) {
            throw new EmailAlreadyRegisteredException("This email is already registered!");
        }

        User user = new User();
        user.setEmail(fullUserDto.getEmail());
        user.setFirstName(fullUserDto.getFirstName());
        user.setLastName(fullUserDto.getLastName());
        user.setPassword(encodePassword().encode(fullUserDto.getPassword()));
        user.setGender(fullUserDto.getGender());
        user.setEnabled(false);
        user.setRole(Role.USER);
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
                    verificationService.setVerificationLink(token.getToken()));
    }

    public void editUser(FullUserDto input) throws MessagingException, IOException{
        User user = getLoggedUser();
        user.setEmail(input.getEmail());
        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setGender(input.getGender());
        userRepository.saveAndFlush(user);
    }

    public User loginUser(LoginUserDto input) {

        User user = userRepository.findOneByEmail(input.getEmail()).orElseThrow(
                () -> new UserDoesNotExistException("User with this email does not exist"));
        if (!encodePassword().matches(input.getPassword(), user.getPassword())) {
            throw new PasswordDoesNotMatchException("Password does not match");
        }
        if(!user.isEnabled()){
            throw new UserIsNotVerifiedException("The email is not verified");
        }
        return user;
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public UserDetailsService getUserByUsername() {
        return username -> userRepository.findOneByEmail(username).orElseThrow();
    }

    public User getLoggedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
    return userRepository
        .findOneByEmail(currentUser)
        .orElseThrow(() -> new UserDoesNotExistException("User not found!"));
    }

    public UserDto getUserProfile(){
        User user = getLoggedUser();

        return new UserDto(user.getFirstName(), user.getLastName(), user.getEmail(), user.getGender());
    }
}