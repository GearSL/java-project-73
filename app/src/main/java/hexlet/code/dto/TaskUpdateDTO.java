package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class TaskUpdateDTO {
    @NotBlank
    @Length(min = 1)
    private String name;
    private String description;
    private Long executorId;
    @NotBlank
    private Long taskStatusId;
}
