package com.volasoftware.tinder.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class User extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    String firstName;  //2-50
    String lastName;  //2-50
    String email;     //x@x.cc
    String password;  //min 8
    Gender gender;
}
