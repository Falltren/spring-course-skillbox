package com.fallt.task_tracker.mapper;

import com.fallt.task_tracker.dto.TaskRq;
import com.fallt.task_tracker.dto.TaskRs;
import com.fallt.task_tracker.dto.TaskSimpleRs;
import com.fallt.task_tracker.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
    TaskRs toDto(Task task);

    TaskSimpleRs toSimpleDto(Task task);

}
