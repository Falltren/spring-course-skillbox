package com.fallt.news_service.dto.request;

import com.fallt.news_service.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterRq {

    @Length(min = 3, max = 20, message = "Длина имени должна составлять от 3 до 20 символов")
    private String name;

    @Email(message = "Неверный формат email", regexp = "^[A-z0-9._%+-]+@[A-z0-9.-]+\\.[A-z]{2,6}$")
    private String email;

    @Password(message = "Неверный формат пароля. Пароль должен состоять из букв, цифр и символов. Обязательно содержать заглавную латинскую букву, цифру и иметь длину не менее 8 символов")
    @NotBlank(message = "Поле не может быть пустым")
    private String password;

    @NotBlank(message = "Поле не может быть пустым")
    private String confirmPassword;

}
