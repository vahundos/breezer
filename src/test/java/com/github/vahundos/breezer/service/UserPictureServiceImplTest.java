package com.github.vahundos.breezer.service;

import com.github.vahundos.breezer.exception.EntityNotFoundException;
import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserPicture;
import com.github.vahundos.breezer.repository.UserPictureRepository;
import com.github.vahundos.breezer.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPictureServiceImplTest {

    private static final long USER_ID = 1L;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPictureRepository userPictureRepository;

    private UserPictureServiceImpl userPictureService;

    private User user;

    @BeforeEach
    public void setUp() {
        this.userPictureService = new UserPictureServiceImpl(userRepository, userPictureRepository);
        this.user = new User();

        when(userRepository.getOne(USER_ID)).thenReturn(user);
    }

    @Test
    void getByUserId_ReturnsUserPicture_WhenPictureExists() {
        var userPicture = new UserPicture();
        when(userPictureRepository.findByUser(user)).thenReturn(Optional.of(userPicture));

        UserPicture actualUserPicture = userPictureService.getByUserId(USER_ID);
        assertThat(actualUserPicture).isSameAs(userPicture);
    }

    @Test
    void getByUserId_ThrowsEntityNotFoundException_WhenPictureDoesntExist() {
        when(userPictureRepository.findByUser(user)).thenReturn(Optional.empty());

        EntityNotFoundException exception = catchThrowableOfType(() -> userPictureService.getByUserId(USER_ID),
                                                                 EntityNotFoundException.class);
        assertThat(exception.getMessage()).isEqualTo(String.format("User picture by userId=%d not found", USER_ID));
    }

    @Test
    void save_UpdatesPicture_WhenPictureAlreadyExists() {
        UserPicture userPicture = Mockito.spy(UserPicture.class);
        when(userPictureRepository.findByUser(user)).thenReturn(Optional.of(userPicture));

        byte[] pictureBytes = new byte[]{1, 2, 3, 4};
        userPictureService.save(pictureBytes, USER_ID);

        verify(userPicture, times(1)).setPicture(pictureBytes);
        verify(userPictureRepository, times(1)).save(userPicture);
    }

    @Test
    void save_SavesPicture_WhenPictureDoesntExist() {
        when(userPictureRepository.findByUser(user)).thenReturn(Optional.empty());

        byte[] pictureBytes = new byte[]{1, 2, 3, 4};
        userPictureService.save(pictureBytes, USER_ID);

        var userPicture = new UserPicture(null, user, pictureBytes);
        verify(userPictureRepository, times(1)).save(eq(userPicture));
    }
}