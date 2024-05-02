package com.fallt.news_service.dto.request;

import lombok.Data;

@Data
public class RegisterRq {

    private String name;

    private String email;

    private String password;

    private String confirmPassword;

}
