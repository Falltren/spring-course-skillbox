package com.example.contactlist.controller;

import com.example.contactlist.entity.Contact;
import com.example.contactlist.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/contacts")
public class ContactController {

    private final ContactService contactService;

    @Value("${app.redirect.baseurl}")
    private String baseUrl;

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("contacts", contactService.getAll());
        return "index";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "create";
    }

    @PostMapping("/create")
    public String save(@ModelAttribute Contact contact) {
        contactService.save(contact);
        return "redirect:" + baseUrl;
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Contact contact = contactService.getContactById(id);
        model.addAttribute("contact", contact);
        return "create";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute Contact contact) {
        contactService.update(contact);
        return "redirect:" + baseUrl;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        contactService.delete(id);
        return "redirect:" + baseUrl;
    }
}
