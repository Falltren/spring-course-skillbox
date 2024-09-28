package com.fallt.task_tracker.util;

import com.fallt.task_tracker.entity.TaskStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class StatusConverter implements Converter<TaskStatus, String> {
    @Override
    public String convert(TaskStatus status) {
        return status.name().toLowerCase();
    }
}
