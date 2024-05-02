package com.fallt.news_service.dto.request;

import lombok.Data;

@Data
public class NewsFilter {

    private Integer offset;

    private Integer limit;

    private String category;

    private String author;
}
