package com.fallt.news_service.dto.request;

import lombok.Data;

@Data
public class UserUpdateRq {

    private String name;

    private String email;

    private String password;
}
