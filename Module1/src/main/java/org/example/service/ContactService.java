package org.example.service;

import lombok.Getter;
import org.example.model.Contact;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class ContactService {

    @Value("${app.contact.save}")
    private String path;
    @Getter
    private final Set<Contact> contacts = new HashSet<>();

    private final static String FULL_NAME_FORMAT = "Контакт указывается в формате:\n"
            + "Иванов Иван Иванович;+89091112233;email@email.com";
    private final static Map<String, String> COMMAND_LIST = Map.of(
            "EXIT", "Завершение приложения",
            "LIST", "Вывод всех контактов",
            "ADD", "Добавление контакта в список контактов. " + FULL_NAME_FORMAT,
            "SAVE", "Сохранение контактов из списка контактов в файл"
    );

    public void start() {
        while (true) {
            System.out.println("Введите команду, для получения списка доступных команд введите HELP");
            String input = new Scanner(System.in).nextLine();
            String[] command = input.split(" ", 2);
            switch (command[0]) {
                case "HELP" -> printCommandList();
                case "LIST" -> printContacts();
                case "ADD" -> addContact(command[1]);
                case "SAVE" -> saveContacts();
                case "EXIT" -> {
                    return;
                }
                default -> System.out.println("Unknown command");
            }
        }
    }

    private void saveContacts() {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(path))) {
            for (Contact contact : contacts) {
                String line = contact.getFullName() + ";" + contact.getPhoneNumber() + ";" + contact.getEmail();
                out.write(line);
                out.newLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addContact(String inputData) {
        String[] elements = inputData.split(";");
        if (!checkFullName(elements[0])) {
            System.out.println("Неверно введено ФИО");
        } else if (!checkPhoneNumber(elements[1])) {
            System.out.println("Введен неверный номер телефона. Повторите ввод");
        } else if (!checkEmail(elements[2])) {
            System.out.println("Введен неверный электронный адрес. Повторите ввод");
        } else {
            contacts.add(new Contact(elements[0], elements[1], elements[2]));
            System.out.println("Контакт сохранен");
        }
    }

    private void printContacts() {
        if (contacts.isEmpty()){
            System.out.println("Список контактов пуст");
        }
        for (Contact contact : contacts) {
            System.out.println(contact.getFullName() + ";" + contact.getPhoneNumber() + ";" + contact.getEmail());
        }
    }

    private void printCommandList() {
        for (Map.Entry<String, String> entry : COMMAND_LIST.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    private boolean checkFullName(String fullName) {
        String[] elements = fullName.split(" ");
        if (elements.length != 3) {
            return false;
        }
        for (String element : elements) {
            if (!element.matches("[А-Я][а-яё]+")) {
                return false;
            }
        }
        return true;
    }

    private boolean checkPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\+8[\\d]{10}");
    }

    private boolean checkEmail(String email) {
        return email.matches(".+@.+\\.[a-z]{3,4}");
    }

}
