package com.volasoftware.tinder.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "user")
public class User extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "FIRST_NAME")
    String firstName;
    @Column(name = "LAST_NAME")
    String lastName;
    @Column(name = "EMAIL")
    String email;
    @Column(name = "PASSWORD")
    String password;
    @Column(name = "GENDER")
    @Enumerated(value = EnumType.STRING)
    Gender gender;

    @Column(name = "CREATED_DATE")
    LocalDateTime createdDate;

    @Column(name = "LAST_MODIFIED")
    LocalDateTime lastModified;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @PrePersist
    public void init() {
        createdDate = LocalDateTime.now();
        lastModified = LocalDateTime.now();
    }

}
