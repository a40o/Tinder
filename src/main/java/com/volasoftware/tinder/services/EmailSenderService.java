package com.volasoftware.tinder.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

@Service
public class EmailSenderService {

    private  JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private  String emailSender;

//    public EmailSenderService(JavaMailSender mailSender, String emailSender) {
//        this.mailSender = mailSender;
//        this.emailSender = emailSender;
//    }

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