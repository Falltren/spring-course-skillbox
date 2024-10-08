package com.fallt.news_service.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewsFilter {

    private Integer offset;

    private Integer limit;

    private String category;

    private String author;
}
