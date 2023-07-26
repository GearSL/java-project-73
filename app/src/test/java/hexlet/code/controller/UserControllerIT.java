package hexlet.code.controller;

import hexlet.code.controllers.UserController;
import hexlet.code.dto.JwtRequestDTO;
import hexlet.code.dto.JwtResponseDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.model.User;
import hexlet.code.reporsitory.UserRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestUtils utils;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String WELCOME = "Welcome to Spring";
    private static final String BASE_URL = "/api";
    private static final String USER_CONTROLLER_PATH = "/api" + UserController.USER_CONTROLLER_PATH;

    @AfterEach
    void clear() {
        utils.tearDown();
    }

    @BeforeEach
    void init() throws Exception {
        // create new user
        mockMvc.perform(post(USER_CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(utils.getTestUserCreationDto(TestUtils.TEST_EMAIL)))
        ).andExpect(status().isOk());

        mockMvc.perform(post(USER_CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(utils.getTestUserCreationDto(TestUtils.TEST_EMAIL_2)))
        ).andExpect(status().isOk());
    }

    @Test
    void checkWelcome() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/welcome")
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains(WELCOME);
    }

    @Test
    void createUser() {
        User user = utils.getUserByEmail(TestUtils.TEST_EMAIL);
        //assertThat(response.getStatus()).isEqualTo(200);
        assertThat(user.getEmail()).isEqualTo(TestUtils.TEST_EMAIL);
    }

    @Test
    void getUser() throws Exception {
        User user = utils.getUserByEmail(TestUtils.TEST_EMAIL);
        MockHttpServletResponse response = mockMvc.perform(
                get(USER_CONTROLLER_PATH + "/" + user.getId())
        ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains(TestUtils.TEST_EMAIL);
    }

    @Test
    void getUsers() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get(USER_CONTROLLER_PATH)
        ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains(TestUtils.TEST_EMAIL);
    }

    @Test
    void updateUser() throws Exception {
        // find created user by email
        User user = utils.getUserByEmail(TestUtils.TEST_EMAIL);
        UserDTO userForUpdate = utils.getTestUserUpdatingDto(TestUtils.TEST_EMAIL);
        String oldPasswordHash = user.getPassword();

        // try to update created user
        MockHttpServletResponse updateResponse = mockMvc.perform(
                put(USER_CONTROLLER_PATH + "/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(userForUpdate))
        ).andReturn().getResponse();

        // checking whether the user's data has been updated
        user = utils.getUserByEmail(TestUtils.TEST_EMAIL);
        assertThat(updateResponse.getStatus()).isEqualTo(200);
        assertThat(user.getEmail()).isEqualTo(userForUpdate.getEmail());
        assertThat(user.getFirstName()).isEqualTo(userForUpdate.getFirstName());
        assertThat(user.getLastName()).isEqualTo(userForUpdate.getLastName());
        // making sure that the password has been changed
        assertThat(user.getPassword()).isNotEqualTo(oldPasswordHash);
    }

    @Test
    void deleteUserOwnRecord() throws Exception {
        // find created user by email
        User user = utils.getUserByEmail(TestUtils.TEST_EMAIL);
        assertThat(user).isNotNull();

        // try to authenticate
        JwtRequestDTO requestDTO = new JwtRequestDTO(TestUtils.TEST_EMAIL, TestUtils.TEST_PASSWORD);
        MockHttpServletResponse response = mockMvc.perform(
                post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsBytes(requestDTO))
        ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(200);

        // try to delete user
        JwtResponseDTO responseDTO = MAPPER.readValue(response.getContentAsString(), JwtResponseDTO.class);
        mockMvc.perform(
                delete(USER_CONTROLLER_PATH + "/" + user.getId())
                        .header("Authorization", "Bearer " + responseDTO.getToken())
        ).andExpect(status().isOk());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> utils.getUserByEmail(TestUtils.TEST_EMAIL), "No value present");
        assertThat(exception.getMessage()).isEqualTo("No value present");
    }

    @Test
    void deleteSomeoneElseUser() throws Exception {
        // find created user by email
        User user = utils.getUserByEmail(TestUtils.TEST_EMAIL);
        assertThat(user).isNotNull();

        // try to authenticate
        JwtRequestDTO requestDTO = new JwtRequestDTO(TestUtils.TEST_EMAIL, TestUtils.TEST_PASSWORD);
        MockHttpServletResponse response = mockMvc.perform(
                post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsBytes(requestDTO))
        ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(200);

        //try to delete user and check
        JwtResponseDTO responseDTO = MAPPER.readValue(response.getContentAsString(), JwtResponseDTO.class);
        User user2 = utils.getUserByEmail(TestUtils.TEST_EMAIL_2);
        mockMvc.perform(
                delete(USER_CONTROLLER_PATH + "/" + user2.getId())
                        .header("Authorization", "Bearer " + responseDTO.getToken())
        ).andExpect(status().is4xxClientError());
        assertThat(utils.getUserByEmail(TestUtils.TEST_EMAIL_2)).isNotNull();
    }
}
