package com.github.vahundos.breezer.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_pictures")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class UserPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Lob
    private byte[] picture;
}
