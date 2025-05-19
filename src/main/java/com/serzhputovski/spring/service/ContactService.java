package com.serzhputovski.spring.service;

import com.serzhputovski.spring.entity.Contact;
import com.serzhputovski.spring.entity.User;

import java.util.List;

public interface ContactService {
    List<Contact> findByUser(User user);
    Contact save(Contact contact);
    void deleteById(Long id);
}
