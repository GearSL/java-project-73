package hexlet.code.service;

import hexlet.code.dto.StatusDTO;
import hexlet.code.model.TaskStatus;
import hexlet.code.reporsitory.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {
    private final StatusRepository statusRepository;

    @Override
    public TaskStatus createStatus(StatusDTO statusDTO) {
        TaskStatus taskStatus = new TaskStatus(statusDTO.getName());
        return statusRepository.save(taskStatus);
    }

    @Override
    public TaskStatus updateStatus(int id, StatusDTO statusDTO) {
        TaskStatus taskStatus = getStatusById(id).orElseThrow();
        taskStatus.setName(statusDTO.getName());
        return statusRepository.save(taskStatus);
    }

    @Override
    public String deleteStatus(int id) {
        statusRepository.findById(id).orElseThrow();
        statusRepository.deleteById(id);
        return "Status successful deleted";
    }

    @Override
    public Optional<TaskStatus> getStatusById(int id) {
        return statusRepository.findById(id);
    }

    @Override
    public List<TaskStatus> getAllStatuses() {
        return statusRepository.findAll();
    }
}
