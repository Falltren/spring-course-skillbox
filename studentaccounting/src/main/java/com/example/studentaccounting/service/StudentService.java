package com.example.studentaccounting.service;

import com.example.studentaccounting.event.AddEvent;
import com.example.studentaccounting.event.DeleteEvent;
import com.example.studentaccounting.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ShellComponent
@RequiredArgsConstructor
public class StudentService {

    private final ApplicationEventPublisher publisher;

    private Map<Integer, Student> storage = new ConcurrentHashMap<>();

    @ShellMethod(key = "add")
    public void addStudent(String firstName, String lastName, int age) {
        int id = storage.size() + 1;
        Student student = Student.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .age(age)
                .build();
        storage.put(id, student);
        publisher.publishEvent(new AddEvent(student));
    }

    @ShellMethod(key = "del")
    public void deleteStudent(int id) {
        if (storage.containsKey(id)) {
            storage.remove(id);
            publisher.publishEvent(new DeleteEvent(id));
        } else {
            System.out.printf("Student with id: %d not found%n", id);
        }
    }

    @ShellMethod(key = "dall")
    public void clearStorage() {
        storage.clear();
    }

    @ShellMethod(key = "print")
    public void printAllStudents() {
        for (Map.Entry<Integer, Student> entry : storage.entrySet()) {
            System.out.println(entry.getValue());
        }
    }


}
