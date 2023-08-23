package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class TaskUpdateDTO {
    @NotBlank
    @Length(min = 1)
    private String name;
    private String description;
    private Long executorId;
    @NotNull
    private Long taskStatusId;
}
