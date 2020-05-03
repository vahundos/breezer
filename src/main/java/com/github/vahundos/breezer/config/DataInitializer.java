package com.github.vahundos.breezer.config;

import com.github.vahundos.breezer.model.User;
import com.github.vahundos.breezer.model.UserPicture;
import com.github.vahundos.breezer.model.UserRole;
import com.github.vahundos.breezer.model.UserStatus;
import com.github.vahundos.breezer.repository.UserPictureRepository;
import com.github.vahundos.breezer.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

@Profile("dev")
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner dataLoader(UserRepository userRepository, UserPictureRepository userPictureRepository,
                                        PasswordEncoder passwordEncoder) {
        return (args -> {
            userRepository.save(new User(null, "Ivan", "Ivanov", "ivanovivan", "ivanov@mail.net", UserStatus.ACTIVATED,
                                         passwordEncoder.encode("password1"), Set.of(UserRole.USER, UserRole.ADMIN)));
            userRepository.save(new User(null, "Petro", "Petrov", "petro23", "petrvo@mail.net", UserStatus.ACTIVATED,
                                         passwordEncoder.encode("password2"), Set.of(UserRole.USER)));
            userRepository.save(new User(null, "Dmytro", "Dmytrov", "dima23", "dmytro@mail.net", UserStatus.BANNED,
                                         passwordEncoder.encode("password3"), Set.of(UserRole.USER)));
            userRepository.save(new User(null, "Semen", "Semenov", "semen1", "semen@mail.net", UserStatus.REGISTERED,
                                         passwordEncoder.encode("password4"), Set.of(UserRole.USER)));

            Path path = Paths.get("/home/volodymyr/file1.png");
            byte[] bytes = Files.readAllBytes(path);

            userPictureRepository.save(new UserPicture(null, userRepository.getOne(1L), bytes));
        });
    }
}
