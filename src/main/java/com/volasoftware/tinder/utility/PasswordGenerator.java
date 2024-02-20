package com.volasoftware.tinder.utility;

import org.apache.commons.lang3.RandomStringUtils;

public class PasswordGenerator {
    public String generatePassword(){
        String passwordCharacters = "1234567890abcdefghijklmnopqrstuvwxyz";
        return RandomStringUtils.random(8, passwordCharacters);
    }
}
