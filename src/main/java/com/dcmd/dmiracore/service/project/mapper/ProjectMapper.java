package com.dcmd.dmiracore.service.project.mapper;

import com.dcmd.dmiracore.model.Project;
import com.dcmd.dmiracore.model.Task;
import com.dcmd.dmiracore.model.User;
import com.dcmd.dmiracore.payload.project.response.ProjectResponse;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {
    public ProjectResponse mapEntityToResponse(Project project) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Set<String> usernames = mapUsersToUsernames(project);
        Set<String> tasks = mapTasksToTaskNames(project);

        return ProjectResponse.Builder.builder()
                .id(project.getId())
                .name(project.getName())
                .tag(project.getTag())
                .createdBy(project.getCreatedBy().getUsername())
                .createDate(project.getCreateDate().format(formatter))
                .modifiedBy(project.getModifiedBy().getUsername())
                .modifyDate(project.getModifyDate().format(formatter))
                .assignedUsers(usernames)
                .tasks(tasks)
                .build();
    }

    private Set<String> mapTasksToTaskNames(Project project) {
        return project.getTasks().stream()
                .map(Task::getName)
                .collect(Collectors.toSet());
    }

    private Set<String> mapUsersToUsernames(Project project) {
        return project.getAssignedUsers().stream()
                .map(User::getUsername)
                .collect(Collectors.toSet());
    }
}
