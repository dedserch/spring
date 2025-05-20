package com.serzhputovski.spring.service.impl;

import com.serzhputovski.spring.entity.Role;
import com.serzhputovski.spring.entity.User;
import com.serzhputovski.spring.exception.ResourceNotFoundException;
import com.serzhputovski.spring.repository.UserRepository;
import com.serzhputovski.spring.service.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void findAll_ReturnsAllUsers() {
        List<User> list = List.of(new User(), new User(), new User());
        when(userRepo.findAll()).thenReturn(list);

        List<User> result = service.findAll();

        assertEquals(3, result.size());
    }

    @Test
    void save_ShouldAddRoleAndSave() {
        User u = new User();
        Role role = new Role();
        role.setName("ROLE_USER");
        when(roleService.findByRoleName("ROLE_USER")).thenReturn(role);
        when(userRepo.save(u)).thenReturn(u);

        User saved = service.save(u);

        assertTrue(saved.getRoles().contains(role));
        verify(userRepo).save(u);
    }

    @Test
    void findByUsername_WhenExists() {
        User u = new User();
        u.setUsername("test");
        when(userRepo.findByUsername("test")).thenReturn(Optional.of(u));

        User found = service.findByUsername("test");

        assertSame(u, found);
    }

    @Test
    void findByUsername_WhenNotExists_Throws() {
        when(userRepo.findByUsername("nope")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.findByUsername("nope"));
    }

    @Test
    void existsByUsernameAndEmail() {
        when(userRepo.existsByUsername("u1")).thenReturn(true);
        when(userRepo.existsByEmail("e1")).thenReturn(false);

        assertTrue(service.existsByUsername("u1"));
        assertFalse(service.existsByEmail("e1"));
    }

    @Test
    void loadUserByUsername_WhenFound() {
        User u = new User();
        u.setUsername("anna");
        u.setPassword("pwd");
        Role r = new Role();
        r.setName("ROLE_USER");
        u.addRole(r);
        when(userRepo.findByUsername("anna")).thenReturn(Optional.of(u));

        UserDetails details = service.loadUserByUsername("anna");

        assertEquals("anna", details.getUsername());
        assertEquals("pwd", details.getPassword());
        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_WhenNotFound_Throws() {
        when(userRepo.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("ghost"));
    }
}