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

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "EXPIRATION_DATE")
    private LocalDateTime expirationDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User user() {
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

    public Token(String token,
                 LocalDateTime createdAt,
                 LocalDateTime expirationDate,
                 User user) {
        this.token = token;
        this.expirationDate = expirationDate;
        this.user = user;
    }
}
