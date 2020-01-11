package com.github.vahundos.breezer.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.vahundos.breezer.view.UserViews;
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
    @JsonView(UserViews.WithoutSensitiveData.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(UserViews.WithoutSensitiveData.class)
    private String firstName;

    @JsonView(UserViews.WithoutSensitiveData.class)
    private String secondName;

    @JsonView(UserViews.WithoutSensitiveData.class)
    private String nickname;

    @JsonView(UserViews.WithoutSensitiveData.class)
    private String email;

    @Enumerated(EnumType.STRING)
    @JsonView(UserViews.WithoutSensitiveData.class)
    private UserStatus status;

    private String password;
}
