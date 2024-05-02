package com.fallt.news_service.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class OneNewsRs {

    private String title;

    private String text;

    private String category;

    private List<CommentRs> comments;
}
