package com.example.contactlist.service;

import com.example.contactlist.entity.Contact;
import com.example.contactlist.repository.ContactRepository;
import com.example.exception.ContactNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public List<Contact> getAll() {
        return contactRepository.findAll();
    }

    public Contact getContactById(Long id){
        return contactRepository.findById(id).orElseThrow(() -> new ContactNotFoundException("Контакт с ID: " + id + " не найден"));
    }

    public Contact save(Contact contact) {
        return contactRepository.save(contact);
    }

    public Contact update(Contact contact) {
        return contactRepository.update(contact);
    }

    public void delete(Long id) {
        contactRepository.deleteById(id);
    }
}
