package com.fallt.task_tracker.controller;

import com.fallt.task_tracker.dto.UserRq;
import com.fallt.task_tracker.dto.UserRs;
import com.fallt.task_tracker.entity.RoleType;
import com.fallt.task_tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
    @GetMapping("/{id}")
    public Mono<UserRs> getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
    @GetMapping
    public Flux<UserRs> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserRs> createUser(@RequestBody UserRq userRq, @RequestParam RoleType roleType) {
        return userService.createUser(userRq, roleType);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
    @PutMapping("/{id}")
    public Mono<UserRs> updateUser(@PathVariable String id, @RequestBody UserRq dto) {
        return userService.updateUser(id, dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUserById(@PathVariable String id) {
        return userService.deleteUserById(id);
    }
}
