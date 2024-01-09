package com.example.studentaccounting.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DeleteEventListener {

    @EventListener
    public void listen(DeleteEvent deleteEvent) {
        System.out.println("Student with id: " + deleteEvent.getId() + " has been deleted");
    }
}
