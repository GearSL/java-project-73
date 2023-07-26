package hexlet.code.utils;

import hexlet.code.dto.UserDTO;
import hexlet.code.model.User;
import hexlet.code.reporsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestUtils {
    @Autowired
    private UserRepository userRepository;
    public static final String TEST_EMAIL = "email@email.com";
    public static final String TEST_EMAIL_2 = "email_2@email.com";
    public static final String TEST_PASSWORD = "pwd";

    public UserDTO getTestUserCreationDto(String email) {
        return new UserDTO(
                email,
                "firstname",
                "lastname",
                TEST_PASSWORD
        );
    }

    public UserDTO getTestUserUpdatingDto(String email) {
        return new UserDTO(
                email,
                "firstname updated",
                "lastname updated",
                "pwd updated"
        );
    }


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public void tearDown() {
        userRepository.deleteAll();
    }
}
