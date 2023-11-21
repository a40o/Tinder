package com.volasoftware.tinder.services;

import com.volasoftware.tinder.model.User;
import com.volasoftware.tinder.model.Verification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
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

@Service
public class EmailSenderService {

    private JavaMailSender mailSender;
    private final ResourceLoader resourceLoader;

    public EmailSenderService(JavaMailSender mailSender, ResourceLoader resourceLoader) {
        this.mailSender = mailSender;
        this.resourceLoader = resourceLoader;
    }

    public void sendEmail(User user,
                          Verification token,
                          String sender,
                          String subject,
                          String contentType) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress(sender));
        message.setRecipients(MimeMessage.RecipientType.TO,user.getEmail());
        message.setSubject(subject);
        message.setContent(getEmailContent(token.getToken()),contentType);
        mailSender.send(message);
    }

    private String getEmailContent(String token) throws IOException{

        Resource emailResource = resourceLoader.getResource("classpath:emailResources/ConfirmationPage.html");

        File emailFile = emailResource.getFile();
        Path path = Path.of(emailFile.getPath());
        String emailContent = Files.readString(path);

        return  emailContent.replace("{{token}}" , "http://localhost:8080/verify/" + token);
    }
}
