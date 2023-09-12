package hexlet.code.service;

import hexlet.code.dto.TaskDTO;
import hexlet.code.model.Task;
import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task createTask(TaskDTO taskDTO);
    Task updateTask(Long id, TaskDTO taskDTO);
    void deleteTask(Long id);
    Optional<Task> getTaskById(Long id);
    List<Task> getAllTaskList();
}
