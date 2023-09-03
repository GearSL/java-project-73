package hexlet.code.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.model.Task;
import hexlet.code.reporsitory.TaskRepository;
import hexlet.code.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;

@RestController
@RequiredArgsConstructor
@EnableSpringDataWebSupport
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
public class TaskController {

    private final TaskService taskService;
    private final TaskRepository taskRepository;
    public static final String ID = "/{id}";
    public static final String TASK_CONTROLLER_PATH = "/tasks";
    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(@taskRepository.findById(#id).get().getAuthor().getId())
            .get().getEmail() == authentication.getName()
        """;

    @PostMapping
    public Task createTask(@RequestBody @Valid TaskDTO taskDTO) {
        return taskService.createTask(taskDTO);
    }

    @PutMapping(ID)
    public Task updateTask(@PathVariable Long id, @RequestBody @Valid TaskUpdateDTO taskUpdateDTO) {
        return taskService.updateTask(id, taskUpdateDTO);
    }

    @PreAuthorize(ONLY_OWNER_BY_ID)
    @DeleteMapping(ID)
    public String deleteTask(@PathVariable Long id) {
        return taskService.deleteTask(id);
    }

    @GetMapping(ID)
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id).orElseThrow();
    }

    @GetMapping
    @ResponseBody
    public Iterable<Task> getAllTasks(@QuerydslPredicate(root = Task.class) Predicate predicate) {
        return taskRepository.findAll(predicate);
    }

}
