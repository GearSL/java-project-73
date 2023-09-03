package hexlet.code.service;

import java.util.Optional;
import hexlet.code.dto.UserDTO;
import hexlet.code.model.User;
import java.util.List;

public interface UserService {
    Optional<User> getUser(Long id);
    List<User> getUsers();
    User createUser(UserDTO userDTO);
    User updateUser(Long id, UserDTO userDTO);
    String deleteUser(Long id);
}
