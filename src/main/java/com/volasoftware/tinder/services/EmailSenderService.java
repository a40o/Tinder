package com.volasoftware.tinder.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail,
                          String subject,
                          String body,
                          String content) throws IOException, MessagingException {

        String file = new String(Files.readAllBytes(Paths.get(content)));
        //SimpleMailMessage message= new SimpleMailMessage();
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom("ang3lkirilov@gmail.com");
        //message.setTo(toEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, toEmail);
        message.setText(body);
        message.setSubject(subject);
        message.setContent(file,"text/html; charset=utf8");

        mailSender.send(message);
    }
}
