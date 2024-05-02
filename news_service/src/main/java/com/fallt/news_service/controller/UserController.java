package com.fallt.news_service.controller;

import com.fallt.news_service.dto.request.RegisterRq;
import com.fallt.news_service.dto.response.UserRs;
import com.fallt.news_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public UserRs create(RegisterRq registerRq) {
        return userService.create(registerRq);
    }
}
