package com.fallt.news_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateNewsRq {

    @NotNull(message = "Идентификатор новости должен быть указан")
    private Long id;

    private String title;

    private String text;

    private String category;
}
