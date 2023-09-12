package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TaskDTO {
    @NotBlank
    @Length(min = 1)
    private String name;
    private String description;
    private Long executorId;
    private List<Long> labelIds;
    @NotNull
    private Long taskStatusId;

    public TaskDTO(String taskName, String taskDescription, Long executorId, Long statusId) {
        this.name = taskName;
        this.description = taskDescription;
        this.executorId = executorId;
        this.taskStatusId = statusId;
    }
}
