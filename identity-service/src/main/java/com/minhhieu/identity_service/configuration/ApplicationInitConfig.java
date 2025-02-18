package com.minhhieu.identity_service.configuration;

import com.minhhieu.identity_service.entity.Role;
import com.minhhieu.identity_service.entity.Users;
import com.minhhieu.identity_service.repository.RoleRepository;
import com.minhhieu.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findById("ADMIN").get());
            if(userRepository.findByUsername("admin").isEmpty()){
                Users user= Users.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();
                userRepository.save(user);
            }
            log.warn("Admin user has been created default with username: admin and password: admin");


        };
    }
}
