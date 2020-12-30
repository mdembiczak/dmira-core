package com.dcmd.dmiracore.service.task.mapper;

import com.dcmd.dmiracore.model.Task;
import com.dcmd.dmiracore.payload.task.TaskResponse;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class TaskMapper {
    public TaskResponse mapEntityToResponse(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TaskResponse.Builder builder = TaskResponse.Builder.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .projectName(task.getProject().getName())
                .createdBy(task.getCreatedBy().getUsername())
                .createDate(task.getCreateDate().format(formatter))
                .modifiedBy(task.getModifiedBy().getUsername())
                .modifyDate(task.getModifyDate().format(formatter))
                .state(task.getState().name());

        if (task.getAssignedTo() != null) {
            builder.assignedTo(task.getAssignedTo().getUsername());
        }
        return builder.build();
    }
}
