package com.github.vahundos.breezer.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.vahundos.breezer.view.UserViews;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
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

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @JsonView(UserViews.WithoutSensitiveData.class)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    private Set<UserRole> roles = new HashSet<>();

    @JoinColumn(name = "picture_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UserPicture picture;
}
