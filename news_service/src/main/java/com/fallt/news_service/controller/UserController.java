package com.fallt.news_service.controller;

import com.fallt.news_service.aop.Accessible;
import com.fallt.news_service.aop.CheckType;
import com.fallt.news_service.dto.request.RegisterRq;
import com.fallt.news_service.dto.request.UserUpdateRq;
import com.fallt.news_service.dto.response.UserRs;
import com.fallt.news_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRs create(@Valid @RequestBody RegisterRq registerRq) {
        return userService.create(registerRq);
    }

    @Accessible(checkType = CheckType.USER)
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public UserRs update(@PathVariable Long id, @RequestBody UserUpdateRq request) {
        return userService.update(id, request);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserRs> getAllUsers(@RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "1") Integer limit) {
        return userService.getAllUsers(offset, limit);
    }

    @Accessible(checkType = CheckType.USER)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public UserRs getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @Accessible(checkType = CheckType.USER)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
