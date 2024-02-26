package com.volasoftware.tinder.services;

import com.volasoftware.tinder.exception.InvalidVerificationTokenException;
import com.volasoftware.tinder.exception.UserAlreadyVerifiedException;
import com.volasoftware.tinder.exception.UserDoesNotExistException;
import com.volasoftware.tinder.model.User;
import com.volasoftware.tinder.model.Verification;
import com.volasoftware.tinder.repository.UserRepository;
import com.volasoftware.tinder.repository.VerificationRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
public class VerificationService{

    @Value("${host_link}")
    private String hostLink;
    private final VerificationRepository verificationRepository;
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;
    private final ResourceLoader resourceLoader;

    public void saveVerificationToken(Verification token){

        verificationRepository.save(token);
    }

    public VerificationService(VerificationRepository verificationRepository,
                               UserRepository userRepository,
                               EmailSenderService emailSenderService,
                               ResourceLoader resourceLoader) {
            this.verificationRepository = verificationRepository;
            this.userRepository = userRepository;
            this.emailSenderService = emailSenderService;
            this.resourceLoader = resourceLoader;
    }
        public boolean verifyUser(String token){

            Verification tokenEntity = verificationRepository.findByToken(token)
                    .orElseThrow(() -> new InvalidVerificationTokenException("Invalid token"));

            if(tokenEntity.getExpirationDate().isAfter(LocalDateTime.now())){
                User userToVerify = tokenEntity.getUser();
                userToVerify.setEnabled(true);
                userRepository.save(userToVerify);
                return true;
            }
            return false;
        }

        public void resendVerificationEmail(String email) throws MessagingException, IOException {

            User user = userRepository.findOneByEmail(email).orElseThrow(
                    () -> new UserDoesNotExistException("This user does not exist!")
            );

            if (user.isEnabled()){
                throw new UserAlreadyVerifiedException("User is already verified!");
            }

            verificationRepository.findTokenByUserId(user.getId()).ifPresent(verificationRepository::delete);

            Verification newToken = new Verification();
            newToken.setUser(user);
            newToken.setToken(UUID.randomUUID().toString());
            newToken.setCreatedDate(LocalDateTime.now());
            newToken.setExpirationDate(LocalDateTime.now().plusDays(2));
            verificationRepository.saveAndFlush(newToken);

            emailSenderService.sendEmail("Verification",
                    Collections.singleton(user.getEmail()),
                    setVerificationLink(newToken.getToken()));
        }

        public String setVerificationLink(String token) throws IOException{

            Resource emailResource = resourceLoader.getResource("classpath:emailResources/ConfirmationPage.html");

            File emailFile = emailResource.getFile();
            Path path = Path.of(emailFile.getPath());
            String emailContent = Files.readString(path);

            return  emailContent.replace("{{token}}" , hostLink + "api/v1/users/verify/" + token);
        }

        public String injectNewPassword(String password) throws IOException{
            Resource emailResource = resourceLoader.getResource("classpath:emailResources/ForgottenPassword.html");

            File emailFile = emailResource.getFile();
            Path path = Path.of(emailFile.getPath());
            String emailContent = Files.readString(path);

            return emailContent.replace("{{password}}",password);
        }
}