package com.fallt.news_service.service;

import com.fallt.news_service.dto.request.RegisterRq;
import com.fallt.news_service.dto.request.UserUpdateRq;
import com.fallt.news_service.exception.BadRequestException;
import com.fallt.news_service.exception.EntityNotFoundException;
import com.fallt.news_service.mapper.UserMapper;
import com.fallt.news_service.model.User;
import com.fallt.news_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User create(RegisterRq request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Пароли должны совпадать");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Пользователь с указанным email существует");
        }
        return userRepository.save(UserMapper.INSTANCE.toEntity(request));
    }

    public User update(Long id, UserUpdateRq request) {
        if (request.getEmail() != null && userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Указанный email уже используется другим пользователем");
        }
        User user = getUserById(id);
        UserMapper.INSTANCE.updateUserFromDto(request, user);
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException(MessageFormat.format("Пользователь с ID: {0} не найден", id));
        }
        return optionalUser.get();
    }

    public List<User> getAllUsers(Integer offset, Integer limit) {
        return userRepository.findAll(PageRequest.of(offset, limit)).getContent();
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
