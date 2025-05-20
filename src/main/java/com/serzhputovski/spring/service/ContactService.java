package com.serzhputovski.spring.service;

import com.serzhputovski.spring.entity.Contact;
import com.serzhputovski.spring.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactService {
    Page<Contact> findByUser(User user, Pageable pageable);
    Contact save(Contact contact);
    void deleteById(Long id);
}
