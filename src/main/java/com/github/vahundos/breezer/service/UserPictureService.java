package com.github.vahundos.breezer.service;

import com.github.vahundos.breezer.model.UserPicture;

public interface UserPictureService {

    UserPicture getByUserId(long userId);

    void save(byte[] picture, long userId);
}
