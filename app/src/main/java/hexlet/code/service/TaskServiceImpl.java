package hexlet.code.service;

import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.reporsitory.TaskStatusRepository;
import hexlet.code.reporsitory.TaskRepository;
import hexlet.code.reporsitory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    @Override
    public Task createTask(TaskDTO taskDTO) {
        TaskStatus taskStatus = taskStatusRepository.findById(taskDTO.getTaskStatusId()).orElseThrow();
        User author = userRepository.findById(taskDTO.getAuthorId()).orElseThrow();
        User executor = userRepository.findById(taskDTO.getExecutorId()).orElseThrow();

        Task task = new Task();
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setAuthor(author);
        task.setExecutor(executor);
        task.setTaskStatus(taskStatus);
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, TaskUpdateDTO taskUpdateDTO) {
        TaskStatus taskStatus = taskStatusRepository.findById(taskUpdateDTO.getTaskStatusId()).orElseThrow();
        User executor = userRepository.findById(taskUpdateDTO.getExecutorId()).orElseThrow();

        Task task = new Task();
        task.setName(taskUpdateDTO.getName());
        task.setDescription(taskUpdateDTO.getDescription());
        task.setExecutor(executor);
        task.setTaskStatus(taskStatus);
        return taskRepository.save(task);
    }

    @Override
    public String deleteTask(Long id) {
        taskRepository.deleteById(id);
        return "Task successful deleted";
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> getAllTaskList() {
        return taskRepository.findAll();
    }

}
