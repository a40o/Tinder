package com.volasoftware.tinder.services;

import com.volasoftware.tinder.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

@Service
public class EmailSenderService {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String emailSender;
    private final UserRepository userRepository;

    public EmailSenderService(JavaMailSender mailSender,
                              ResourceLoader resourceLoader,
                              UserRepository userRepository) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
    }

    public void sendEmail(String emailSubject,
                          Set<String> userEmails,
                          String emailContent) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress(emailSender));
        for (String email : userEmails) {
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
        }
        message.setSubject(emailSubject);
        message.setContent(emailContent,"text/html; charset=utf-8");
        mailSender.send(message);
    }
}