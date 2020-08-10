package com.github.vahundos.breezer.service;

import com.github.vahundos.breezer.dto.UserRegistrationDto;
import com.github.vahundos.breezer.exception.DuplicateUserException;
import com.github.vahundos.breezer.exception.EntityNotFoundException;
import com.github.vahundos.breezer.exception.IncompatibleUserStatusException;
import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserStatus;
import com.github.vahundos.breezer.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        this.userService = new UserServiceImpl(modelMapper, userRepository, passwordEncoder);
    }

    @Test
    void register_savesUser_WhenEmailOrNicknameDontExist() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.findByNickname(any())).thenReturn(Optional.empty());

        when(modelMapper.map(any(), any())).thenReturn(new User());

        User savedUser = new User();
        when(userRepository.save(any())).thenReturn(savedUser);

        User actualUser = userService.register(new UserRegistrationDto());
        assertThat(actualUser).isEqualTo(savedUser);
    }

    @Test
    void register_throwsDuplicateUserException_WhenUserWithEmailAlreadyExists() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));

        DuplicateUserException exception = catchThrowableOfType(() -> userService.register(new UserRegistrationDto()),
                                                                DuplicateUserException.class);
        assertThat(exception.getMessage()).contains("User with email");
        assertThat(exception.getMessage()).contains("already exists");
    }

    @Test
    void register_throwsDuplicateUserException_WhenUserWithNicknameAlreadyExists() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.findByNickname(any())).thenReturn(Optional.of(new User()));

        DuplicateUserException exception = catchThrowableOfType(() -> userService.register(new UserRegistrationDto()),
                                                                DuplicateUserException.class);
        assertThat(exception.getMessage()).contains("User with nickname");
        assertThat(exception.getMessage()).contains("already exists");
    }
}