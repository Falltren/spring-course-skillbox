package com.fallt.news_service.service;

import com.fallt.news_service.dto.request.RegisterRq;
import com.fallt.news_service.dto.response.UserRs;
import com.fallt.news_service.exception.BadRequestException;
import com.fallt.news_service.mapper.UserMapper;
import com.fallt.news_service.model.User;
import com.fallt.news_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserRs create(RegisterRq request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Пароли должны совпадать");
        }
        if (userRepository.getByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Пользователь с указанным email существует");
        }
        User user = userRepository.save(UserMapper.INSTANCE.toEntity(request));
        return UserMapper.INSTANCE.toResponseDto(user);
    }
}
