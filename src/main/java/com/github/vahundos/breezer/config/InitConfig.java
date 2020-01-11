package com.github.vahundos.breezer.config;

import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserStatus;
import com.github.vahundos.breezer.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitConfig {

    @Bean
    public CommandLineRunner loadData(UserRepository userRepository) {
        return (args -> {
            userRepository.save(new User(null, "Ivan", "Ivanov", "ivanov.ivan", "ivanov@mail.net", UserStatus.REGISTERED, "password1"));
            userRepository.save(new User(null, "Petro", "Petrov", "petro23", "petrvo@mail.net", UserStatus.ACTIVE, "password2"));
        });
    }
}
