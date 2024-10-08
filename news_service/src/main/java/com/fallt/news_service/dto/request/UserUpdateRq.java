package com.fallt.news_service.dto.request;

import com.fallt.news_service.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserUpdateRq {

    private String name;

    private String email;

    private String password;

    private Set<Role> roles;
}
