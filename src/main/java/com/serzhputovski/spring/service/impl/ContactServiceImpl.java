package com.serzhputovski.spring.service.impl;

import com.serzhputovski.spring.entity.Contact;
import com.serzhputovski.spring.entity.User;
import com.serzhputovski.spring.repository.ContactRepository;
import com.serzhputovski.spring.service.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repo;
    private static final Logger log = LogManager.getLogger(ContactServiceImpl.class);

    public ContactServiceImpl(ContactRepository repo) {
        this.repo = repo;
    }

    @Override
    public Page<Contact> findByUser(User user, Pageable pageable) {
        log.debug("Finding contacts for user {}", user.getUsername());
        Page<Contact> page = repo.findByUser(user, pageable);
        log.info("Found {} contacts for user {}", page.getTotalElements(), user.getUsername());
        return page;
    }

    @Override
    public Contact save(Contact contact) {
        log.debug("Saving contact for user {}: {}", contact.getUser().getUsername(), contact);
        Contact saved = repo.save(contact);
        log.info("Contact saved with id {}", saved.getId());
        return saved;
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting contact with id {}", id);
        repo.deleteById(id);
        log.info("Contact with id {} deleted", id);
    }
}
