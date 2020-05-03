package com.github.vahundos.breezer.repository;

import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserPicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPictureRepository extends JpaRepository<UserPicture, Long> {

    Optional<UserPicture> findByUser(User user);
}
