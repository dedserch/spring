package com.serzhputovski.spring.repository;

import com.serzhputovski.spring.entity.Contact;
import com.serzhputovski.spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByUser(User user);
}
