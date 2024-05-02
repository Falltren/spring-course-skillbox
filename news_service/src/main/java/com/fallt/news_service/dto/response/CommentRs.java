package com.fallt.news_service.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentRs {

    private String text;

    private LocalDateTime createAt;
}
