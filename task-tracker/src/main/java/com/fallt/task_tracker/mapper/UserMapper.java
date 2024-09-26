package com.fallt.task_tracker.mapper;

import com.fallt.task_tracker.dto.UserDto;
import com.fallt.task_tracker.entity.User;
import org.mapstruct.*;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = getMapper(UserMapper.class);

    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDto dto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserDto request, @MappingTarget User user);
}
