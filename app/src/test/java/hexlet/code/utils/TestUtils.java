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
    private final UserDTO userCreationDTO = new UserDTO(
            TEST_EMAIL,
            "firstname",
            "lastname",
            "pwd"
    );

    private final UserDTO userUpdatingDTO = new UserDTO(
            TEST_EMAIL,
            "firstname updated",
            "lastname updated",
            "pwd updated"
    );

    public UserDTO getTestUserCreationDto() {
        return userCreationDTO;
    }

    public UserDTO getTestUserUpdatingDto() {
        return userUpdatingDTO;
    }


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(TestUtils.TEST_EMAIL).orElseThrow();
    }

    public void tearDown() {
        userRepository.deleteAll();
    }
}
