package hexlet.code.service;

import hexlet.code.dto.UserDTO;
import hexlet.code.model.User;
import hexlet.code.reporsitory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Optional<User> getUser(long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow();
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public String deleteUser(long id) {
        userRepository.findById(id).orElseThrow();
        userRepository.deleteById(id);

        return "User successful deleted";
    }
}
