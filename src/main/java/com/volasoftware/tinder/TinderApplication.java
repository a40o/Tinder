package com.volasoftware.tinder;

import com.volasoftware.tinder.services.AuditableService;
import com.volasoftware.tinder.services.EmailSenderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.AuditorAware;

import java.io.IOException;

@SpringBootApplication
public class TinderApplication {
//    @Autowired
//    private EmailSenderService senderService;

    public static void main(String[] args) {
        SpringApplication.run(TinderApplication.class, args);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void sendMail() throws MessagingException, IOException {
//        senderService.sendEmail("angelkirilov6@protonmail.com",
//                "subject",
//                "u are gay",
//                "/home/a4ko/Codes/Java/Tinder/src/main/resources/emailResources/ConfirmationPage.html"
//                );
//    }
}
