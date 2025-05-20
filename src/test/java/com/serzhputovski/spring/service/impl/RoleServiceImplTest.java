package com.serzhputovski.spring.service.impl;

import com.serzhputovski.spring.entity.Role;
import com.serzhputovski.spring.exception.ResourceNotFoundException;
import com.serzhputovski.spring.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository repo;

    @InjectMocks
    private RoleServiceImpl service;

    @Test
    void save_ShouldCallRepository() {
        Role role = new Role();
        when(repo.save(role)).thenReturn(role);

        Role result = service.save(role);

        assertSame(role, result);
        verify(repo).save(role);
    }

    @Test
    void findByRoleName_Found() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        when(repo.findByName("ROLE_ADMIN")).thenReturn(Optional.of(role));

        Role found = service.findByRoleName("ROLE_ADMIN");

        assertSame(role, found);
    }

    @Test
    void findByRoleName_NotFound_Throws() {
        when(repo.findByName("X")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.findByRoleName("X"));
    }
}