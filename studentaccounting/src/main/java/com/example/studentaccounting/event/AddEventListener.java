package com.example.studentaccounting.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AddEventListener {

    @EventListener
    public void listen(AddEvent addEvent) {
        System.out.println("Student has been added: " + addEvent.getStudent());
    }
}
