package hexlet.code.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDTO {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Length(min = 1)
    private String firstName;
    @NotBlank
    @Length(min = 1)
    private String lastName;
    @NotBlank
    @Length(min = 3)
    private String password;
}
