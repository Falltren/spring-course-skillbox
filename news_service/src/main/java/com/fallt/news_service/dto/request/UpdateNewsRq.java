package com.fallt.news_service.dto.request;

import lombok.Data;

@Data
public class UpdateNewsRq {

    private Long newsId;

    private String title;

    private String text;

    private String category;
}
