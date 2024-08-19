package com.fallt.news_service.mapper;

import com.fallt.news_service.dto.request.RegisterRq;
import com.fallt.news_service.dto.request.UserUpdateRq;
import com.fallt.news_service.dto.response.UserRs;
import com.fallt.news_service.model.User;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = getMapper(UserMapper.class);

    @Mapping(target = "news", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toEntity(RegisterRq registerRq);

    @Mapping(target = "timestamp", expression = "java(System.currentTimeMillis())")
    UserRs toResponseDto(User user);

    @Mapping(target = "timestamp", expression = "java(System.currentTimeMillis())")
    List<UserRs> toListDto(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "news", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserUpdateRq request, @MappingTarget User user);
}
