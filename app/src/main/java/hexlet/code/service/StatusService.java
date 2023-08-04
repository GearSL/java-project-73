package hexlet.code.service;

import hexlet.code.dto.StatusDTO;
import hexlet.code.model.TaskStatus;
import java.util.List;
import java.util.Optional;

public interface StatusService {
    TaskStatus createStatus(StatusDTO statusDTO);
    TaskStatus updateStatus(int id, StatusDTO statusDTO);
    String deleteStatus(int id);
    Optional<TaskStatus> getStatusById(int id);
    List<TaskStatus> getAllStatuses();
}
