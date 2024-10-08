package com.fallt.news_service.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateNewsRq {

    private String title;

    private String text;

    private String category;
}
