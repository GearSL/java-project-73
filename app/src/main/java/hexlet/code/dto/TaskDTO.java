package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TaskDTO {
    @NotBlank
    @Length(min = 1)
    private String name;
    private String description;
    @NotNull
    private Long authorId;
    private Long executorId;
    @NotNull
    private Long taskStatusId;
}
