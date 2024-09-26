package com.fallt.task_tracker.service;

import com.fallt.task_tracker.dto.UserDto;
import com.fallt.task_tracker.exception.EntityNotFoundException;
import com.fallt.task_tracker.mapper.UserMapper;
import com.fallt.task_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Flux<UserDto> getAllUsers() {
        return userRepository.findAll()
                .map(UserMapper.INSTANCE::toDto)
                .switchIfEmpty(Flux.empty());
    }

    public Mono<UserDto> getUserById(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(MessageFormat.format("User with ID: {0} not found", id))))
                .map(UserMapper.INSTANCE::toDto);
    }

    public Mono<UserDto> createUser(UserDto dto) {
        return userRepository.save(UserMapper.INSTANCE.toEntity(dto))
                .map(UserMapper.INSTANCE::toDto);
    }

    public Mono<UserDto> updateUser(String id, UserDto dto) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(MessageFormat.format("User with ID: {0} not found", id))))
                .flatMap(existedUser -> {
                    UserMapper.INSTANCE.updateUserFromDto(dto, existedUser);
                    return userRepository.save(existedUser).map(UserMapper.INSTANCE::toDto);
                });
    }

    public Mono<Void> deleteById(String id) {
        return userRepository.deleteById(id);
    }
}
