package com.fallt.news_service.dto.request;

import lombok.Data;

@Data
public class NewsRq {

    private String title;

    private String text;

    private String category;
}
