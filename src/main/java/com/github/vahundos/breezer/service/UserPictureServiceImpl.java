package com.github.vahundos.breezer.service;

import com.github.vahundos.breezer.exception.EntityNotFoundException;
import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserPicture;
import com.github.vahundos.breezer.repository.UserPictureRepository;
import com.github.vahundos.breezer.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.lang.String.format;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserPictureServiceImpl implements UserPictureService {

    private final UserRepository userRepository;

    private final UserPictureRepository userPicRepository;

    @Override
    public UserPicture getByUserId(long userId) {
        return userPicRepository.findByUser(userRepository.getOne(userId))
                                .orElseThrow(() -> new EntityNotFoundException(format("User picture by userId=%d not found",
                                                                                      userId)));
    }

    @Override
    @Transactional
    public void save(byte[] picture, long userId) {
        User userReference = userRepository.getOne(userId);

        Optional<UserPicture> userPicture = userPicRepository.findByUser(userReference);
        if (userPicture.isPresent()) {
            UserPicture retrievedUserPicture = userPicture.get();
            retrievedUserPicture.setPicture(picture);
            userPicRepository.save(retrievedUserPicture);
            return;
        }

        userPicRepository.save(new UserPicture(null, userReference, picture));
    }
}
