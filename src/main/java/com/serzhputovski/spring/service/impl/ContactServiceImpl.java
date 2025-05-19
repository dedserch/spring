package com.serzhputovski.spring.service.impl;

import com.serzhputovski.spring.entity.Contact;
import com.serzhputovski.spring.entity.User;
import com.serzhputovski.spring.repository.ContactRepository;
import com.serzhputovski.spring.service.ContactService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repo;

    public ContactServiceImpl(ContactRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Contact> findByUser(User user) {
        return repo.findByUser(user);
    }

    @Override
    public Contact save(Contact contact) {
        return repo.save(contact);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
