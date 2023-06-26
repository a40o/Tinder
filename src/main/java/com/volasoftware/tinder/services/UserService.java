package com.volasoftware.tinder.services;

import com.volasoftware.tinder.dto.UserDto;
import com.volasoftware.tinder.model.Gender;
import com.volasoftware.tinder.model.Token;
import com.volasoftware.tinder.model.User;
import com.volasoftware.tinder.repository.TokenRepository;
import com.volasoftware.tinder.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    @Autowired
    private EmailSenderService senderService;

    public UserService(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void registerUser(UserDto userDto) throws MessagingException, IOException {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setGender(Gender.valueOf(userDto.getGender()));
        user.setEnabled(false);
        userRepository.saveAndFlush(user);

        String uuid = UUID.randomUUID().toString();
        Token token = new Token(
                uuid,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                user
        );

        tokenRepository.save(token);

        sendMail();
        //return token;
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void sendMail() throws MessagingException, IOException {

        senderService.sendEmail("angelkirilov6@protonmail.com",
                "subject",
                "u are gay",
                "/home/a4ko/Codes/Java/Tinder/src/main/resources/emailResources/ConfirmationPage.html"
        );
    }
}