package org.example.service;

import jakarta.annotation.PostConstruct;
import org.example.model.Contact;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Set;

@Component
@Profile("init")
public class ContactInit {

    private final ContactService contactService;

    @Value("${app.contact.init}")
    private String initPath;

    public ContactInit(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostConstruct
    public void initContactList() {
        Set<Contact> contacts = contactService.getContacts();
        try (BufferedReader in = new BufferedReader(new FileReader(initPath))) {
            String data;
            while ((data = in.readLine()) != null) {
                String[] elements = data.split(";");
                Contact contact = new Contact(elements[0], elements[1], elements[2]);
                contacts.add(contact);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
