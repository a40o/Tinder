package com.volasoftware.tinder.services;

import com.volasoftware.tinder.dto.UserDto;
import com.volasoftware.tinder.model.Gender;
import com.volasoftware.tinder.model.Token;
import com.volasoftware.tinder.model.User;
import com.volasoftware.tinder.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public UserService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void registerUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setGender(Gender.valueOf(userDto.getGender()));
        userRepository.save(user);

        String uuid = UUID.randomUUID().toString();
        Token token = new Token(
                uuid,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                user
        );

        tokenService.saveToken(token);
        //return token;
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

}