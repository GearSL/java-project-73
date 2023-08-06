package hexlet.code.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.controller.StatusController;
import hexlet.code.controller.UserController;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.model.User;
import hexlet.code.reporsitory.TaskStatusRepository;
import hexlet.code.reporsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan(basePackages = "hexlet.code")
public class TestUtils {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    public static final String TEST_EMAIL = "email@email.com";
    public static final String TEST_EMAIL_2 = "email_2@email.com";
    public static final String TEST_PASSWORD = "pwd";
    public static final String TEST_STATUS = "New";
    public static final String TEST_STATUS_2 = "Draft";
    public static final String BASE_URL = "/api";
    public static final String USER_CONTROLLER_PATH = BASE_URL + UserController.USER_CONTROLLER_PATH;
    public static final String STATUS_CONTROLLER_PATH = BASE_URL + StatusController.STATUS_CONTROLLER_PATH;
    public static final ObjectMapper MAPPER = new ObjectMapper();


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

    public TaskStatusDTO getTestStatusCreationDTO() {
        return new TaskStatusDTO(TEST_STATUS);
    }

    public TaskStatusDTO getTestStatusUpdatingDTO() {
        return new TaskStatusDTO(TEST_STATUS_2);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public Long getExactStatusId() {
        return taskStatusRepository.findAll().get(0).getId();
    }

    public void tearDown() {
        userRepository.deleteAll();
        taskStatusRepository.deleteAll();
    }


//        public void createUser(String email) throws Exception {
//        mockMvc.perform(post(USER_CONTROLLER_PATH)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(MAPPER.writeValueAsString(getTestUserCreationDto(email)))
//        ).andExpect(status().isOk());
//    }

//    public String getTokenForUser(String email, String password) throws Exception {
//        JwtRequestDTO requestDTO = new JwtRequestDTO(email, password);
//        MockHttpServletResponse authResponse = mockMvc.perform(post(BASE_URL + "/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(MAPPER.writeValueAsString(requestDTO))
//        ).andReturn().getResponse();
//        JwtResponseDTO responseDTO = MAPPER.readValue(authResponse.getContentAsString(), JwtResponseDTO.class);
//        return responseDTO.getToken();
//    }
}
