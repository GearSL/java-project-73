package hexlet.code.service;

import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.model.TaskStatus;
import java.util.List;
import java.util.Optional;

public interface TaskStatusService {

    TaskStatus createStatus(TaskStatusDTO taskStatusDTO);
    TaskStatus updateStatus(Long id, TaskStatusDTO taskStatusDTO);
    String deleteStatus(Long id);
    Optional<TaskStatus> getStatusById(Long id);
    List<TaskStatus> getAllStatuses();

}
