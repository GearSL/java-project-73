package hexlet.code.service;

import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.model.TaskStatus;
import hexlet.code.reporsitory.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {
    private final TaskStatusRepository taskStatusRepository;

    @Override
    public TaskStatus createStatus(TaskStatusDTO taskStatusDTO) {
        TaskStatus taskStatus = new TaskStatus(taskStatusDTO.getName());
        return taskStatusRepository.save(taskStatus);
    }

    @Override
    public TaskStatus updateStatus(Long id, TaskStatusDTO taskStatusDTO) {
        TaskStatus taskStatus = getStatusById(id).orElseThrow();
        taskStatus.setName(taskStatusDTO.getName());
        return taskStatusRepository.save(taskStatus);
    }

    @Override
    public void deleteStatus(Long id) {
        taskStatusRepository.findById(id).orElseThrow();
        taskStatusRepository.deleteById(id);
    }

    @Override
    public Optional<TaskStatus> getStatusById(Long id) {
        return taskStatusRepository.findById(id);
    }

    @Override
    public List<TaskStatus> getAllStatuses() {
        return taskStatusRepository.findAll();
    }
}
