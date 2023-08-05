package hexlet.code.controller;

import hexlet.code.dto.StatusDTO;
import hexlet.code.model.TaskStatus;
import hexlet.code.service.StatusService;
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

import static hexlet.code.controller.StatusController.STATUS_CONTROLLER_PATH;

@RequiredArgsConstructor
@RestController
@RequestMapping("${base-url}" + STATUS_CONTROLLER_PATH)
public class StatusController {
    public static final String STATUS_CONTROLLER_PATH = "/statuses";
    public static final String ID = "/{id}";
    private final StatusService statusService;

    @PostMapping
    public TaskStatus createStatus(@RequestBody StatusDTO statusDTO) {
        return statusService.createStatus(statusDTO);
    }

    @PutMapping(ID)
    public TaskStatus updateStatus(@PathVariable int id, @RequestBody StatusDTO statusDTO) {
        return statusService.updateStatus(id, statusDTO);
    }

    @DeleteMapping(ID)
    public String deleteStatus(@PathVariable int id) {
        return statusService.deleteStatus(id);
    }

    @GetMapping(ID)
    public Optional<TaskStatus> getStatusById(@PathVariable int id) {
        return statusService.getStatusById(id);
    }

    @GetMapping
    public List<TaskStatus> getAllStatuses() {
        return statusService.getAllStatuses();
    }
}
