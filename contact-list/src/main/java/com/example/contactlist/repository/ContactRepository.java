package com.example.contactlist.repository;

import com.example.contactlist.entity.Contact;
import com.example.contactlist.mapper.ContactRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ContactRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Contact> findAll() {
        String sql = "SELECT * FROM contacts";
        return jdbcTemplate.query(sql, new ContactRowMapper());
    }
}
