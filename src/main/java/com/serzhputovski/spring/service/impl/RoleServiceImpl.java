package com.serzhputovski.spring.service.impl;

import com.serzhputovski.spring.entity.Role;
import com.serzhputovski.spring.exception.ResourceNotFoundException;
import com.serzhputovski.spring.repository.RoleRepository;
import com.serzhputovski.spring.service.RoleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private static final Logger log = LogManager.getLogger(RoleServiceImpl.class);
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(Role role) {
        log.debug("Attempting to save role {}", role);

        var newRole = this.roleRepository.save(role);

        log.info("Role saved: {}", newRole);
        return newRole;
    }

    @Override
    public Role findByRoleName(String roleName) {
        log.debug("Attempting to find role by roleName {}", roleName);

        var role = this.roleRepository.findByName(roleName).orElseThrow(
                () -> {
                    var message = "Role with username: '%s' was not found".formatted(roleName);
                    log.warn(message);
                    return new ResourceNotFoundException(message);
                }
        );

        log.info("Role found: {}", role);
        return role;
    }
}
