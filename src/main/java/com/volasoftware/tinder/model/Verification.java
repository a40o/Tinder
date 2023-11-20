package com.volasoftware.tinder.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "token")
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TOKEN")
    private String token;
    @Temporal(TIMESTAMP)
    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;
    @Temporal(TIMESTAMP)
    @Column(name = "EXPIRATION_DATE")
    private LocalDateTime expirationDate;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER")
    private User user;

    public User getId() {
        return user;
    }

    public void setId(User userId) {
        this.user = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
}