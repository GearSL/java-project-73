package hexlet.code.controllers;

import hexlet.code.exceptions.ErrorResponse;
import hexlet.code.dto.UserDTO;
import hexlet.code.model.User;
import hexlet.code.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("${base-url}" + USER_CONTROLLER_PATH)
public class UserController {
    public static final String USER_CONTROLLER_PATH = "/users";
    public static final String ID = "/{id}";
    private final UserService userService;
    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;

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
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public String deleteUser(@PathVariable long id) {
        return userService.deleteUser(id);

    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(403, "Access denied"));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(NoSuchElementException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, "The requested item was not found"));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse(422, "Validation error"));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(500, "Unknown error"));
    }
}
