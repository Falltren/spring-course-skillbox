package com.fallt.task_tracker.service;

import com.fallt.task_tracker.dto.UserRq;
import com.fallt.task_tracker.dto.UserRs;
import com.fallt.task_tracker.entity.RoleType;
import com.fallt.task_tracker.entity.User;
import com.fallt.task_tracker.exception.EntityNotFoundException;
import com.fallt.task_tracker.mapper.UserMapper;
import com.fallt.task_tracker.repository.UserRepository;
import com.fallt.task_tracker.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public Flux<UserRs> getAllUsers() {
        return userRepository.findAll()
                .map(UserMapper.INSTANCE::toDto)
                .switchIfEmpty(Flux.empty());
    }

    public Mono<UserRs> getUserById(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException(MessageFormat.format("User with ID: {0} not found", id))))
                .map(UserMapper.INSTANCE::toDto);
    }

    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException(MessageFormat.format("User with username: {0} not found", username))
                ));
    }

    public Mono<UserRs> createUser(UserRq dto, RoleType roleType) {
        User user = UserMapper.INSTANCE.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(Set.of(roleType));
        return userRepository.save(user)
                .map(UserMapper.INSTANCE::toDto);
    }

    public Mono<UserRs> updateUser(String id, UserRq dto) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException(MessageFormat.format("User with ID: {0} not found", id))))
                .flatMap(existedUser -> {
                    UserMapper.INSTANCE.updateUserFromDto(dto, existedUser);
                    return userRepository.save(existedUser)
                            .map(UserMapper.INSTANCE::toDto);
                });
    }

    public Mono<Void> deleteUserById(String id) {
        return userRepository.deleteById(id);
    }

    public Flux<User> getUsersBySetId(Set<String> ids) {
        return userRepository.getUsersByIdIn(ids);
    }

    public Mono<String> getCurrentUserId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(AppUserDetails.class)
                .map(AppUserDetails::getUserId);
    }
}
