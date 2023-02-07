package com.volasoftware.tinder.model;

public enum Gender {
    MALE("male", "resources/male.png"),
    FEMALE("female", "resources/female.png"),
    OTHER("other", "resources/other.png");

    public final String label;
    public final String imagePath;

    private Gender(String label, String imagePath) {
        this.label = label;
        this.imagePath = imagePath;
    }
}
