package com.fallt.news_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CommentRq {

    @NotNull(message = "Поле newsId обязательно должно содержать идентификатор новости")
    private Long newsId;

    @Length(min = 3, max = 500, message = "Количество символов в комментарии должно быть в диапазоне 3-500")
    private String text;

}