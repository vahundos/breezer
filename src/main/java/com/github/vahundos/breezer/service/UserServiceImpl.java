package com.github.vahundos.breezer.service;

import com.github.vahundos.breezer.dto.UserRegistrationDto;
import com.github.vahundos.breezer.exception.DuplicateUserException;
import com.github.vahundos.breezer.exception.EntityNotFoundException;
import com.github.vahundos.breezer.exception.IncompatibleUserStatusException;
import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserStatus;
import com.github.vahundos.breezer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;

    private final UserRepository repository;

    @Override
    public User get(long id) {
        log.debug("Getting user by id={}", id);
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
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
