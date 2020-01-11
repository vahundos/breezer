package com.github.vahundos.breezer.service;

import com.github.vahundos.breezer.exception.DuplicateUserException;
import com.github.vahundos.breezer.exception.EntityNotFoundException;
import com.github.vahundos.breezer.exception.IncompatibleUserStatusException;
import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserStatus;
import com.github.vahundos.breezer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public User get(long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    public User register(User user) {
        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateUserException("email", user.getEmail());
        }
        if (repository.findByNickname(user.getNickname()).isPresent()) {
            throw new DuplicateUserException("nickname", user.getNickname());
        }

        user.setStatus(UserStatus.REGISTERED);
        return repository.save(user);
    }

    @Override
    public User updateStatus(long userId, UserStatus newStatus) {
        if (newStatus == UserStatus.REGISTERED) {
            throw new IncompatibleUserStatusException(newStatus);
        }
        User user = this.get(userId);
        user.setStatus(newStatus);
        return repository.save(user);
    }
}
