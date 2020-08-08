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
    void get_returnsUser_WhenUserExists() {
        var userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        assertThat(userService.get(userId)).isNotNull();
    }

    @Test
    void get_throwsEntityNotFoundException_WhenUserDoesntExist() {
        var userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = catchThrowableOfType(() -> userService.get(userId),
                                                                 EntityNotFoundException.class);

        assertThat(exception.getMessage()).isEqualTo(String.format("User with id=%d not found", userId));
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

        var userRegistrationDto = new UserRegistrationDto();
        DuplicateUserException exception = catchThrowableOfType(() -> userService.register(userRegistrationDto),
                                                                DuplicateUserException.class);
        assertThat(exception.getMessage()).contains("User with email");
        assertThat(exception.getMessage()).contains("already exists");
    }

    @Test
    void register_throwsDuplicateUserException_WhenUserWithNicknameAlreadyExists() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.findByNickname(any())).thenReturn(Optional.of(new User()));

        var userRegistrationDto = new UserRegistrationDto();
        DuplicateUserException exception = catchThrowableOfType(() -> userService.register(userRegistrationDto),
                                                                DuplicateUserException.class);
        assertThat(exception.getMessage()).contains("User with nickname");
        assertThat(exception.getMessage()).contains("already exists");
    }

    @Test
    void updateStatus_tavesUser_WhenStatusIsNotRegistered() {
        var id = 1L;
        var newUserStatus = UserStatus.ACTIVATED;

        when(userRepository.findById(id)).thenReturn(Optional.of(User.builder().id(id).build()));
        when(userRepository.save(any())).thenReturn(User.builder().id(id).status(newUserStatus).build());

        User actualUser = userService.updateStatus(id, newUserStatus);
        assertThat(actualUser.getId()).isEqualTo(id);
        assertThat(actualUser.getStatus()).isEqualTo(newUserStatus);
    }

    @Test
    void updateStatus_throwsIncompatibleUserStatusException_WhenStatusIsRegistered() {
        IncompatibleUserStatusException exception = catchThrowableOfType(() -> userService.updateStatus(1, UserStatus.REGISTERED),
                                                                         IncompatibleUserStatusException.class);
        assertThat(exception.getMessage()).isEqualTo("Can't change status to " + UserStatus.REGISTERED);
    }
}