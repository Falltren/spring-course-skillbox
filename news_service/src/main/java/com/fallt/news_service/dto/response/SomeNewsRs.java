package com.fallt.news_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SomeNewsRs {

    private String title;

    private String text;

    private String category;

    private int count;
}
