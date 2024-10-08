package com.fallt.news_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class NewsRq {

    @Length(min = 3, max = 50, message = "Заголовок новости должен содержать от 3 до 50 символов")
    private String title;

    @Length(min = 10, max = 3000, message = "Объем текста новости должен содержать от 10 до 3000 символов")
    private String text;

    @NotNull(message = "Категория новости должна быть указана")
    private String category;
}
