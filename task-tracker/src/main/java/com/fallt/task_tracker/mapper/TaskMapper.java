package com.fallt.task_tracker.mapper;

import com.fallt.task_tracker.dto.TaskRq;
import com.fallt.task_tracker.dto.TaskFullRs;
import com.fallt.task_tracker.dto.TaskSimpleRs;
import com.fallt.task_tracker.dto.UserDto;
import com.fallt.task_tracker.entity.Task;
import com.fallt.task_tracker.entity.User;
import org.mapstruct.*;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskMapper INSTANCE = getMapper(TaskMapper.class);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "observers", ignore = true)
    @Mapping(target = "assigneeUser", ignore = true)
    @Mapping(target = "id", ignore = true)
    Task toEntity(TaskRq request);

    @Mapping(target = "authorId", ignore = true)
    @Mapping(target = "assigneeId", ignore = true)
    @Mapping(target = "observerIds", ignore = true)
    TaskFullRs toFullDto(Task task);

    TaskSimpleRs toSimpleDto(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "observers", ignore = true)
    @Mapping(target = "assigneeUser", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskFromDto(TaskRq request, @MappingTarget Task task);

}
