package com.serzhputovski.spring.config;

import com.serzhputovski.spring.entity.Role;
import com.serzhputovski.spring.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class RoleInitConfig {

    private final RoleRepository roleRepository;

    public RoleInitConfig(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Bean
    @Transactional
    public CommandLineRunner commandLineRunnerBean() {
        return args -> {
            if (!roleRepository.existsByName("ROLE_USER")) {
                Role userRole = new Role();
                userRole.setName("ROLE_USER");
                roleRepository.save(userRole);
            }
            if (!roleRepository.existsByName("ROLE_ADMIN")) {
                Role adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");
                roleRepository.save(adminRole);
            }
        };
    }
}
