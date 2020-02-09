package com.github.vahundos.breezer.config;

import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserRole;
import com.github.vahundos.breezer.model.UserStatus;
import com.github.vahundos.breezer.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner dataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return (args -> {
            userRepository.save(new User(null, "Ivan", "Ivanov", "ivanovivan", "ivanov@mail.net", UserStatus.ACTIVATED,
                                         passwordEncoder.encode("password1"), Set.of(UserRole.USER, UserRole.ADMIN)));
            userRepository.save(new User(null, "Petro", "Petrov", "petro23", "petrvo@mail.net", UserStatus.ACTIVATED,
                                         passwordEncoder.encode("password2"), Set.of(UserRole.USER)));
        });
    }
}
