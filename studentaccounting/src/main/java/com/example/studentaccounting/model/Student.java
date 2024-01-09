package com.example.studentaccounting.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Student {

    private Integer id;

    private String firstName;

    private String lastName;

    private int age;

    @Override
    public String toString() {
        return firstName + " " + lastName + ", " + age + " age";
    }
}
