package com.github.vahundos.breezer.model;

import javax.persistence.*;

@Entity
@Table(name = "user_pictures")
public class UserPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private byte[] picture;
}
