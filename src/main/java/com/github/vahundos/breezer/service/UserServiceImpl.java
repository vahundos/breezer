package com.github.vahundos.breezer.service;

import com.github.vahundos.breezer.dto.UserLoginDto;
import com.github.vahundos.breezer.dto.UserRegistrationDto;
import com.github.vahundos.breezer.exception.DuplicateUserException;
import com.github.vahundos.breezer.exception.EntityNotFoundException;
import com.github.vahundos.breezer.exception.IncompatibleUserStatusException;
import com.github.vahundos.breezer.exception.InvalidCredentialsException;
import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserStatus;
import com.github.vahundos.breezer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;

    private final UserRepository repository;

    @Override
    public User get(long id) {
        log.debug("Getting user by id={}", id);
        return repository.findById(id)
                         .orElseThrow(() -> new EntityNotFoundException(format("User with id=%d not found", id)));
    }

    @Override
    public User login(UserLoginDto userDto) {
        String login = userDto.getLogin();
        String password = userDto.getPassword();
        log.debug("Trying login user with login={} and password={}", login, password);

        User user = repository.findByNickname(login)
                              .or(() -> repository.findByEmail(login))
                              .orElseThrow(() -> new EntityNotFoundException(format("User not found by login=%s and password=%s",
                                                                                    login, password)));
        if (user.getPassword().equals(password)) {
            return user;
        }

        throw new InvalidCredentialsException(format("Can't auth with login=%s and password=%s", login,
                                                     password));
    }

    @Override
    public User register(UserRegistrationDto userDto) {
        log.debug("Register new user={}", userDto);
        if (repository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new DuplicateUserException("email", userDto.getEmail());
        }
        if (repository.findByNickname(userDto.getNickname()).isPresent()) {
            throw new DuplicateUserException("nickname", userDto.getNickname());
        }

        User user = modelMapper.map(userDto, User.class);
        user.setStatus(UserStatus.REGISTERED);
        return repository.save(user);
    }

    @Override
    public User updateStatus(long userId, UserStatus newStatus) {
        log.debug("Updating status={} for userId={}", newStatus, userId);
        if (newStatus == UserStatus.REGISTERED) {
            throw new IncompatibleUserStatusException(newStatus);
        }
        User user = this.get(userId);
        user.setStatus(newStatus);
        return repository.save(user);
    }
}
