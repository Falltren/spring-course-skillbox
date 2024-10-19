package com.fallt.task_tracker.mapper;

import com.fallt.task_tracker.dto.TaskFullRs;
import com.fallt.task_tracker.dto.TaskRq;
import com.fallt.task_tracker.dto.TaskSimpleRs;
import com.fallt.task_tracker.entity.Task;
import org.mapstruct.*;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskMapper INSTANCE = getMapper(TaskMapper.class);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "observers", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "authorId", ignore = true)
    @Mapping(target = "id", ignore = true)
    Task toEntity(TaskRq request);

    TaskFullRs toFullDto(Task task);

    TaskSimpleRs toSimpleDto(Task task);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "observers", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "authorId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskFromDto(TaskRq request, @MappingTarget Task task);

}
