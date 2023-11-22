package com.volasoftware.tinder.services;

import com.volasoftware.tinder.model.User;
import com.volasoftware.tinder.model.Verification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("{$spring.mail.username}")
    private String emailSender;

    @Value("{$email_subject}")
    private String emailSubject;

    @Value("{$email_content_type}")
    private String emailContentType;

    @Value("{$email_page_path}")
    private String emailPagePath;

    @Value("{$verification_link}")
    private String verificationLink;

    public EmailSenderService(JavaMailSender mailSender, ResourceLoader resourceLoader) {
        this.mailSender = mailSender;
        this.resourceLoader = resourceLoader;
    }

    public void sendEmail(User user,
                          Verification token) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress(emailSender));
        message.setRecipients(MimeMessage.RecipientType.TO,user.getEmail());
        message.setSubject(emailSubject);
        message.setContent(getEmailContent(token.getToken()),emailContentType);
        mailSender.send(message);
    }

    private String getEmailContent(String token) throws IOException{

        Resource emailResource = resourceLoader.getResource(emailPagePath);

        File emailFile = emailResource.getFile();
        Path path = Path.of(emailFile.getPath());
        String emailContent = Files.readString(path);

        return  emailContent.replace("{{token}}" , verificationLink + token);
    }
}
