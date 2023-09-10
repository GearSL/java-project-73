package hexlet.code.service;

import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.reporsitory.LabelRepository;
import hexlet.code.reporsitory.TaskStatusRepository;
import hexlet.code.reporsitory.TaskRepository;
import hexlet.code.reporsitory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final LabelRepository labelRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    @Override
    public Task createTask(TaskDTO taskDTO) {
        TaskStatus taskStatus = taskStatusRepository.findById(taskDTO.getTaskStatusId()).orElseThrow();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User author = null;
        if (!(authentication instanceof AnonymousAuthenticationToken) && authentication != null) {
            author = userRepository.findByEmail(authentication.getName()).orElseThrow();
        }

        User executor = userRepository.findById(taskDTO.getExecutorId()).orElseThrow();
        Set<Label> labels = null;
        if (taskDTO.getLabelIds() != null) {
            labels = taskDTO.getLabelIds().stream()
                    .map(labelId -> labelRepository.findById(labelId).orElseThrow())
                    .collect(java.util.stream.Collectors.toSet());
        }

        Task task = new Task();
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setAuthor(author);
        task.setLabels(labels);
        task.setExecutor(executor);
        task.setTaskStatus(taskStatus);
        if (author != null) {
            task.setAuthor(author);
        }
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, TaskUpdateDTO taskUpdateDTO) {
        TaskStatus taskStatus = taskStatusRepository.findById(taskUpdateDTO.getTaskStatusId()).orElseThrow();
        User author = taskRepository.findById(id).get().getAuthor();
        User executor = userRepository.findById(taskUpdateDTO.getExecutorId()).orElseThrow();

        Task task = new Task();
        task.setName(taskUpdateDTO.getName());
        task.setDescription(taskUpdateDTO.getDescription());
        task.setAuthor(author);
        task.setExecutor(executor);
        task.setTaskStatus(taskStatus);
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
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
