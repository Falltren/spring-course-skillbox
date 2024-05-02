package com.fallt.news_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private LocalDateTime createAt;

    @ManyToOne
    @JoinColumn(name = "news_id", referencedColumnName = "id")
    private News news;
}
