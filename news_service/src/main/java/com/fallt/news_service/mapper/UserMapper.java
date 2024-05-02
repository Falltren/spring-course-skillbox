package com.fallt.news_service.mapper;

import com.fallt.news_service.dto.request.RegisterRq;
import com.fallt.news_service.dto.response.UserRs;
import com.fallt.news_service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = getMapper(UserMapper.class);

    @Mapping(target = "news", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toEntity(RegisterRq registerRq);

    @Mapping(target = "timestamp", ignore = true)
    UserRs toResponseDto(User user);
}
