package com.dcmd.dmiracore.controller.task;

import com.dcmd.dmiracore.payload.messages.ErrorMessageResponse;
import com.dcmd.dmiracore.payload.task.TaskCreationRequest;
import com.dcmd.dmiracore.payload.task.TaskResponse;
import com.dcmd.dmiracore.payload.task.TaskUpdateRequest;
import com.dcmd.dmiracore.service.task.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/task")
public class TaskController {
    final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Get task by name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found given task",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponse.class)))
    })
    @GetMapping("{name}")
    public ResponseEntity<?> getTaskByName(@PathVariable String name) {
        return taskService.getTaskByName(name);
    }


    @Operation(summary = "Create task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskCreationRequest request) {
        return taskService.createTask(request);
    }

    @Operation(summary = "Update task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Unexpected error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RuntimeException.class)))

    })
    @PutMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskUpdateRequest request) {
        return taskService.updateTask(request);
    }
}
