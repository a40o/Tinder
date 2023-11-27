package com.volasoftware.tinder.services;

import com.volasoftware.tinder.exception.InvalidVerificationTokenException;
import com.volasoftware.tinder.exception.UserAlreadyVerifiedException;
import com.volasoftware.tinder.exception.UserDoesNotExistException;
import com.volasoftware.tinder.model.User;
import com.volasoftware.tinder.model.Verification;
import com.volasoftware.tinder.repository.UserRepository;
import com.volasoftware.tinder.repository.VerificationRepository;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationService{

    private final VerificationRepository verificationRepository;
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;

    public void saveVerificationToken(Verification token){

        verificationRepository.save(token);
    }

    public VerificationService(VerificationRepository verificationRepository,
                               UserRepository userRepository,
                               EmailSenderService emailSenderService) {

            this.verificationRepository = verificationRepository;
            this.userRepository = userRepository;
            this.emailSenderService = emailSenderService;
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

            emailSenderService.sendEmail(user, newToken);}
    }