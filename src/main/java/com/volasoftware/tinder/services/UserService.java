package com.volasoftware.tinder.services;

import com.volasoftware.tinder.dto.UserDto;
import com.volasoftware.tinder.exception.EmailAlreadyRegisteredException;
import com.volasoftware.tinder.model.Gender;
import com.volasoftware.tinder.model.User;
import com.volasoftware.tinder.model.Verification;
import com.volasoftware.tinder.repository.UserRepository;
import com.volasoftware.tinder.repository.VerificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
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
    private JavaMailSender mailSender;

    public UserService(UserRepository userRepository,
                       VerificationRepository verificationRepository,
                       ResourceLoader resourceLoader,
                       JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.verificationRepository = verificationRepository;
        this.resourceLoader = resourceLoader;
        this.mailSender = mailSender;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    private String getEmailContent(String token) throws IOException{
        Resource emailResource = resourceLoader.getResource("classpath:emailResources/ConfirmationPage.html");

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

        Verification token = new Verification();
        token.setUserId(user);
        token.setToken(UUID.randomUUID().toString());
        token.setCreatedDate(LocalDateTime.now());
        token.setExpirationDate(LocalDateTime.now().plusDays(2));
        verificationRepository.saveAndFlush(token);

        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress("ang3lkirilov@gmail.com"));
        message.setRecipients(MimeMessage.RecipientType.TO,user.getEmail());
        message.setSubject("Verification");
        message.setContent(getEmailContent(token.getToken()),"text/html; charset=utf-8");
        mailSender.send(message);
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }
}