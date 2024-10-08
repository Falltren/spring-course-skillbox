package com.fallt.news_service.service;

import com.fallt.news_service.dto.request.RegisterRq;
import com.fallt.news_service.dto.request.UserUpdateRq;
import com.fallt.news_service.dto.response.UserRs;
import com.fallt.news_service.entity.User;
import com.fallt.news_service.exception.BadRequestException;
import com.fallt.news_service.exception.EntityNotFoundException;
import com.fallt.news_service.mapper.UserMapper;
import com.fallt.news_service.repository.UserRepository;
import com.fallt.news_service.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserRs create(RegisterRq request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Пользователь с указанным email существует");
        }
        if (userRepository.existsByName(request.getName())) {
            throw new BadRequestException("Пользователь с указанным именем существует");
        }
        User user = UserMapper.INSTANCE.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return UserMapper.INSTANCE.toResponseDto(userRepository.save(user));
    }

    public UserRs update(Long id, UserUpdateRq request) {
        if (request.getEmail() != null && userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Указанный email уже используется другим пользователем");
        }
        if (request.getName() != null && userRepository.findByName(request.getName()).isPresent()) {
            throw new BadRequestException("Пользователь с указанным именем существует");
        }
        User user = findUser(id);
        UserMapper.INSTANCE.updateUserFromDto(request, user);
        return UserMapper.INSTANCE.toResponseDto(userRepository.save(user));
    }

    public UserRs getUserById(Long id) {
        return UserMapper.INSTANCE.toResponseDto(findUser(id));
    }

    public User findUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException(MessageFormat.format("Пользователь с ID: {0} не найден", id));
        }
        return optionalUser.get();
    }

    public Long getIdCurrentUser(){
        AppUserDetails user = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }

    public List<UserRs> getAllUsers(Integer offset, Integer limit) {
        return UserMapper.INSTANCE.toListDto(userRepository.findAll(PageRequest.of(offset, limit)).getContent());
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
