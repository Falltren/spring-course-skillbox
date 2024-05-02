package com.fallt.news_service.dto.response;

import lombok.Data;

@Data
public class SomeNewsRs {

    private String title;

    private String text;

    private String category;

    private int count;
}
