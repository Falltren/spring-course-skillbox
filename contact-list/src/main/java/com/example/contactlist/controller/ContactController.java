package com.example.contactlist.controller;

import com.example.contactlist.entity.Contact;
import com.example.contactlist.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/contacts")
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("contacts", contactService.getAll());
        return "index";
    }

    @PostMapping("/save")
    public String save(@RequestBody Contact contact) {
        contactService.save(contact);
        return "index";
    }
}
