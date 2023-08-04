package hexlet.code.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.controller.UserController;
import hexlet.code.dto.StatusDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.model.User;
import hexlet.code.reporsitory.StatusRepository;
import hexlet.code.reporsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

@Component
@ComponentScan(basePackages = "hexlet.code")
public class TestUtils {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    MockMvc mockMvc;
    public static final String TEST_EMAIL = "email@email.com";
    public static final String TEST_EMAIL_2 = "email_2@email.com";
    public static final String TEST_PASSWORD = "pwd";
    public static final String TEST_STATUS = "New";
    public static final String TEST_STATUS_2 = "Draft";
    public static final String BASE_URL = "/api";
    public static final String USER_CONTROLLER_PATH = BASE_URL + UserController.USER_CONTROLLER_PATH;
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

    public StatusDTO getTestStatusCreationDTO() {
        return new StatusDTO(TEST_STATUS);
    }

    public StatusDTO getTestStatusUpdatingDTO() {
        return new StatusDTO(TEST_STATUS_2);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public int getExactStatusId() {
        return statusRepository.findAll().get(0).getId();
    }

    public void tearDown() {
        userRepository.deleteAll();
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
