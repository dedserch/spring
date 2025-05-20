package com.serzhputovski.spring.service.impl;

import com.serzhputovski.spring.entity.Contact;
import com.serzhputovski.spring.entity.User;
import com.serzhputovski.spring.repository.ContactRepository;
import com.serzhputovski.spring.service.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repo;

    public ContactServiceImpl(ContactRepository repo) {
        this.repo = repo;
    }

    @Override
    public Page<Contact> findByUser(User user, Pageable pageable) {
        return repo.findByUser(user, pageable);
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
