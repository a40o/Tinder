package com.volasoftware.tinder.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "token")
@NoArgsConstructor
public class Token extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "EXPIRATION_DATE")
    private LocalDateTime expirationDate;

    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @PrePersist
    public void init() {
        creationDate = LocalDateTime.now();
        expirationDate = LocalDateTime.now();
    }

    public Token(String token,
                 LocalDateTime createdAt,
                 LocalDateTime expirationDate,
                 User user) {
        this.token = token;
        this.creationDate = createdAt;
        this.expirationDate = expirationDate;
        this.userId = user.getId();
    }
}
