package com.minh1952.identityservice.configuration;

import com.minh1952.identityservice.entity.User;
import com.minh1952.identityservice.enums.Role;
import com.minh1952.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;


@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;


    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args ->{
            if(userRepository.findByUsername("admin").isEmpty()){
                Set<String> roleAdmin = new HashSet<>();
                roleAdmin.add(Role.ADMIN.name());

                User admin = User.builder()
                        .firstName("admin")
                        .username("admin")
                        .roles(roleAdmin)
                        .password(passwordEncoder.encode("admin"))
                        .build();

                userRepository.save(admin);
                log.warn("admin has been created with default password : admin");
            }
        };
    }
}
