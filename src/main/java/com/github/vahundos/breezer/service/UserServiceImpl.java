package com.github.vahundos.breezer.service;

import com.github.vahundos.breezer.AuthorizedUser;
import com.github.vahundos.breezer.dto.UserRegistrationDto;
import com.github.vahundos.breezer.exception.DuplicateUserException;
import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserRole;
import com.github.vahundos.breezer.model.UserStatus;
import com.github.vahundos.breezer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final ModelMapper modelMapper;

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

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
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setStatus(UserStatus.REGISTERED);
        user.getRoles().add(UserRole.USER);
        return repository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.debug("Loading user by username={}", username);
        User user = repository.findByNickname(username)
                              .or(() -> repository.findByEmail(username))
                              .orElseThrow(() -> new UsernameNotFoundException(format("User with username=%s", username)));
        return new AuthorizedUser(user);
    }
}
