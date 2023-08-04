package hexlet.code.reporsitory;

import hexlet.code.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<TaskStatus, Integer> {
}
