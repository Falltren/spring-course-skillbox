package com.fallt.task_tracker.mapper;

import com.fallt.task_tracker.dto.UserRq;
import com.fallt.task_tracker.dto.UserRs;
import com.fallt.task_tracker.entity.User;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = getMapper(UserMapper.class);

    UserRs toDto(User user);

    List<UserRs> toListDto(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toEntity(UserRq dto);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserRq request, @MappingTarget User user);
}
