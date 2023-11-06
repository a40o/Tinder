package com.volasoftware.tinder.services;

import com.volasoftware.tinder.dto.UserDto;
import com.volasoftware.tinder.exception.EmailAlreadyRegisteredException;
import com.volasoftware.tinder.model.Gender;
import com.volasoftware.tinder.model.Token;
import com.volasoftware.tinder.model.User;
import com.volasoftware.tinder.repository.TokenRepository;
import com.volasoftware.tinder.repository.UserRepository;
import com.volasoftware.tinder.repository.VerificationRepository;
import jakarta.mail.MessagingException;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final VerificationRepository verificationRepository;
    private final ResourceLoader resourceLoader;
    private final TokenRepository tokenRepository;
    @Autowired
    private EmailSenderService senderService;

    public UserService(UserRepository userRepository, VerificationRepository verificationRepository, ResourceLoader resourceLoader, TokenRepository tokenRepository, EmailSenderService senderService) {
        this.userRepository = userRepository;
        this.verificationRepository = verificationRepository;
        this.resourceLoader = resourceLoader;
        this.tokenRepository = tokenRepository;
        this.senderService = senderService;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    private String getEmailContent(String token) throws IOException{
        Resource emailResource = resourceLoader.getResource("classpath:email/ConfirmationPage.html");

        File emailFile = emailResource.getFile();
        Path path = Path.of(emailFile.getPath());
        String emailContent = Files.readString(path);

        return  emailContent.replace("{{token}}" , "http://localhost:8080/verify/" + token);
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
                "Verification Email",
                "",
                "/home/a4ko/Codes/Java/Tinder/src/main/resources/emailResources/ConfirmationPage.html"
        );
    }
}