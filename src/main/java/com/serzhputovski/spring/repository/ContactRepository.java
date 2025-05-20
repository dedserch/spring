package com.serzhputovski.spring.repository;

import com.serzhputovski.spring.entity.Contact;
import com.serzhputovski.spring.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Page<Contact> findByUser(User user, Pageable pageable);
}
