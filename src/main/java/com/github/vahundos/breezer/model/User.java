package com.github.vahundos.breezer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String secondName;

    private String nickname;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private String password;
}
