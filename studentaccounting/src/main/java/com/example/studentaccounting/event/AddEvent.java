package com.example.studentaccounting.event;

import com.example.studentaccounting.model.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddEvent {

    private Student student;

}
