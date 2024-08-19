package com.fallt.news_service.dto.response;

import lombok.Data;

import java.time.Instant;

@Data
public class CommentRs {

    private String text;

    private Instant createAt;
}
