package hexlet.code.service;

import hexlet.code.dto.TaskDTO;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final LabelRepository labelRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    @Override
    public Task createTask(TaskDTO taskDTO) {
        return taskRepository.save(fromDto(taskDTO));
    }

    @Override
    public Task updateTask(Long id, TaskDTO taskDTO) {
        return taskRepository.save(fromDto(taskDTO));
    }

    private Task fromDto(final TaskDTO taskDTO) {
        User author = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken) && authentication != null) {
            author = userRepository.findByEmail(authentication.getName()).orElseThrow();
        }

        User executor = Optional.ofNullable(taskDTO.getExecutorId())
                .map(executorId -> userRepository.findById(executorId).orElseThrow())
                .orElse(null);

        TaskStatus taskStatus = Optional.ofNullable(taskDTO.getTaskStatusId())
                .map(taskStatusId -> taskStatusRepository.findById(taskStatusId).orElseThrow())
                .orElse(null);

        Set<Label> labels = null;
        if (taskDTO.getLabelIds() != null) {
            labels = taskDTO.getLabelIds().stream()
                    .map(labelId -> labelRepository.findById(labelId).orElseThrow())
                    .collect(Collectors.toSet());
        }

        return Task.builder()
                .author(author)
                .executor(executor)
                .taskStatus(taskStatus)
                .labels(labels)
                .name(taskDTO.getName())
                .description(taskDTO.getDescription())
                .build();
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
