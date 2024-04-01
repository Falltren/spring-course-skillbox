package com.example.contactlist.entity;

import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("contacts")
@FieldNameConstants
public class Contact {

    @Id
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

}
