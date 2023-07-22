package hexlet.code.controllers;

import hexlet.code.ErrorResponse;
import hexlet.code.dto.UserDTO;
import hexlet.code.model.User;
import hexlet.code.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.NoSuchElementException;

import static hexlet.code.controllers.UserController.USER_CONTROLLER_PATH;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + USER_CONTROLLER_PATH)
public class UserController {
    public static final String USER_CONTROLLER_PATH = "/users";
    public static final String ID = "/{id}";
    private final UserService userService;

    @GetMapping(ID)
    public User getUser(@PathVariable long id) {
        return userService.getUser(id).orElseThrow();
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public User createUser(@RequestBody @Valid UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @PutMapping(ID)
    public User updateUser(@PathVariable long id, @RequestBody @Valid UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }

    @DeleteMapping(ID)
    public String deleteUser(@PathVariable long id) {
        return userService.deleteUser(id);

    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse("Validation error", 422));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(NoSuchElementException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("The requested item was not found", 404));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Unknown error", 500));
    }
}
