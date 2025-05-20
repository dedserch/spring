package com.serzhputovski.spring.controller;

import com.serzhputovski.spring.entity.Contact;
import com.serzhputovski.spring.entity.User;
import com.serzhputovski.spring.service.ContactService;
import com.serzhputovski.spring.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;
    private final UserService userService;

    public ContactController(ContactService contactService, UserService userService) {
        this.contactService = contactService;
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model,
                       Principal principal,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "5") int size) {
        User user = userService.findByUsername(principal.getName());
        Page<Contact> contactsPage = contactService.findByUser(user, PageRequest.of(page, size));
        model.addAttribute("contactsPage", contactsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "contacts/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "contacts/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Contact contact, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        contact.setUser(user);
        contactService.save(contact);
        return "redirect:/contacts";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        contactService.deleteById(id);
        return "redirect:/contacts";
    }
}
