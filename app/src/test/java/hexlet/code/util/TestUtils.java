package hexlet.code.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.controller.TaskStatusController;
import hexlet.code.controller.UserController;
import hexlet.code.dto.JwtRequestDTO;
import hexlet.code.dto.JwtResponseDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.reporsitory.TaskRepository;
import hexlet.code.reporsitory.TaskStatusRepository;
import hexlet.code.reporsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class TestUtils {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private TaskRepository taskRepository;
    private static WebApplicationContext webApplicationContext;
    private static MockMvc mockMvc;
    public static final String TEST_EMAIL_1 = "email@email.com";
    public static final String TEST_EMAIL_2 = "email_2@email.com";
    public static final String TEST_PASSWORD_1 = "pwd";
    public static final String TEST_PASSWORD_2 = "pwd_2";
    public static final String TEST_STATUS_1 = "New";
    public static final String TEST_STATUS_2 = "Draft";
    public static final String TASK_NAME = "Test task";
    public static final String TASK_DESCRIPTION = "Test description";
    public static final String BASE_URL = "/api";
    public static final String USER_CONTROLLER_PATH = BASE_URL + UserController.USER_CONTROLLER_PATH;
    public static final String STATUS_CONTROLLER_PATH = BASE_URL + TaskStatusController.STATUS_CONTROLLER_PATH;

    public static final UserDTO USER_DTO_1 = new UserDTO(
            TEST_EMAIL_1,
            "firstname",
            "lastname",
            TEST_PASSWORD_1);

    public static final UserDTO USER_DTO_2 = new UserDTO(
            TEST_EMAIL_2,
            "firstname updated",
            "lastname updated",
            TEST_PASSWORD_2);

    public static final TaskStatusDTO TASK_STATUS_DTO_1 = new TaskStatusDTO(TEST_STATUS_1);

    public static final TaskStatusDTO TASK_STATUS_DTO_2 = new TaskStatusDTO(TEST_STATUS_2);

    public static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    public void setWebApplicationContext(WebApplicationContext webApplicationContext) {
        TestUtils.webApplicationContext = webApplicationContext;
    }

    public static MockMvc getMockMvc() {
        if (mockMvc == null) {
            mockMvc = MockMvcBuilders
                    .webAppContextSetup(webApplicationContext)
                    .build();
        }
        return mockMvc;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public Long getStatusId() {
        return taskStatusRepository.findAll().get(0).getId();
    }

    public Optional<Task> findByName(String name) {
        return taskRepository.findByName(name);
    }

    public void createUser(UserDTO userDTO) throws Exception {
        MockMvc mockMvc = getMockMvc();
        mockMvc.perform(
                post(USER_CONTROLLER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(userDTO))
        ).andExpect(status().isOk());
    }

    public void createStatus(TaskStatusDTO taskStatusDTO) throws Exception {
        MockMvc mockMvc = getMockMvc();
        mockMvc.perform(
                post(STATUS_CONTROLLER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(taskStatusDTO))
        ).andExpect(status().isOk());
    }

    public void createTask(TaskDTO taskDTO) throws Exception {
        MockMvc mockMvc = getMockMvc();
        mockMvc.perform(post(BASE_URL + "/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(taskDTO))
        ).andExpect(status().isOk());
    }

    public String getJwtToken(String email, String password) throws Exception {
        JwtRequestDTO requestDTO = new JwtRequestDTO(email, password);
        MockHttpServletResponse authResponse = mockMvc.perform(post(BASE_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(requestDTO))
        ).andReturn().getResponse();
        JwtResponseDTO responseDTO = MAPPER.readValue(authResponse.getContentAsString(), JwtResponseDTO.class);
        return responseDTO.getToken();
    }

    public void tearDown() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        taskStatusRepository.deleteAll();
    }
}
