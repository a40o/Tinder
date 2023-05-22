package com.volasoftware.tinder.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "token")
public class Token extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "TOKEN")
    private UUID token;

    @Column(name = "EXPIRATION_DATE")
    LocalDateTime expirationDate;

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

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    @PrePersist
    public void init(){
        expirationDate = LocalDateTime.now();
    }
}
