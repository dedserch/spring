package com.serzhputovski.spring.service.impl;

import com.serzhputovski.spring.entity.Contact;
import com.serzhputovski.spring.entity.User;
import com.serzhputovski.spring.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {

    @Mock
    private ContactRepository repo;

    @InjectMocks
    private ContactServiceImpl service;

    @Test
    void findByUser_DelegatesToRepo() {
        User user = new User();
        Pageable pageable = PageRequest.of(0, 5);
        Page<Contact> page = new PageImpl<>(List.of(new Contact(), new Contact()));
        when(repo.findByUser(user, pageable)).thenReturn(page);

        Page<Contact> result = service.findByUser(user, pageable);

        assertSame(page, result);
        verify(repo).findByUser(user, pageable);
    }

    @Test
    void save_DelegatesToRepo() {
        Contact contact = new Contact();
        when(repo.save(contact)).thenReturn(contact);

        Contact result = service.save(contact);

        assertSame(contact, result);
        verify(repo).save(contact);
    }

    @Test
    void deleteById_DelegatesToRepo() {
        service.deleteById(123L);
        verify(repo).deleteById(123L);
    }
}