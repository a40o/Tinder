package com.volasoftware.tinder.utility;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Bean;

public class PasswordGenerator {
    @Bean
    public static String generatePassword(){
        String passwordCharacters = "1234567890abcdefghijklmnopqrstuvwxyz";
        return RandomStringUtils.random(8, passwordCharacters);
    }
}
