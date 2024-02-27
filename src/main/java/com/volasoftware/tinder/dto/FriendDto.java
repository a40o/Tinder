package com.volasoftware.tinder.dto;

import com.volasoftware.tinder.model.Gender;

public class FriendDto {
    private String firstName;
    private String lastName;
    private Gender gender;
    private int age;
    private double location;

    public FriendDto(String firstName,
                     String lastName,
                     Gender gender,
                     int age,
                     long location){
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.location = location;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getLocation() {
        return location;
    }

    public void setLocation(double location) {
        this.location = location;
    }
}
