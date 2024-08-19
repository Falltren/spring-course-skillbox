package com.fallt.news_service.controller;

import com.fallt.news_service.dto.request.RegisterRq;
import com.fallt.news_service.dto.request.UserUpdateRq;
import com.fallt.news_service.dto.response.UserRs;
import com.fallt.news_service.mapper.UserMapper;
import com.fallt.news_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public UserRs create(@Valid @RequestBody RegisterRq registerRq) {
        return UserMapper.INSTANCE.toResponseDto(userService.create(registerRq));
    }

    @PutMapping("/update/{id}")
    public UserRs update(@PathVariable Long id, @RequestBody UserUpdateRq request) {
        return UserMapper.INSTANCE.toResponseDto(userService.update(id, request));
    }

    @GetMapping
    public List<UserRs> getAllUsers(@RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "1") Integer limit) {
        return UserMapper.INSTANCE.toListDto(userService.getAllUsers(offset, limit));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
