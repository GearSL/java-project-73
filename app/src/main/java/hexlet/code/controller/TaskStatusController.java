package hexlet.code.controller;

import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.model.TaskStatus;
import hexlet.code.service.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Optional;

import static hexlet.code.controller.TaskStatusController.STATUS_CONTROLLER_PATH;

@RequiredArgsConstructor
@RestController
@RequestMapping("${base-url}" + STATUS_CONTROLLER_PATH)
public class TaskStatusController {

    public static final String STATUS_CONTROLLER_PATH = "/statuses";
    public static final String ID = "/{id}";
    private final TaskStatusService taskStatusService;

    @PostMapping
    public TaskStatus createStatus(@RequestBody TaskStatusDTO taskStatusDTO) {
        return taskStatusService.createStatus(taskStatusDTO);
    }

    @PutMapping(ID)
    public TaskStatus updateStatus(@PathVariable Long id, @RequestBody TaskStatusDTO taskStatusDTO) {
        return taskStatusService.updateStatus(id, taskStatusDTO);
    }

    @DeleteMapping(ID)
    public String deleteStatus(@PathVariable Long id) {
        return taskStatusService.deleteStatus(id);
    }

    @GetMapping(ID)
    public Optional<TaskStatus> getStatusById(@PathVariable Long id) {
        return taskStatusService.getStatusById(id);
    }

    @GetMapping
    public List<TaskStatus> getAllStatuses() {
        return taskStatusService.getAllStatuses();
    }

}
