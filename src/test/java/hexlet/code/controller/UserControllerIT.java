package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.UserDTO;
import hexlet.code.model.User;
import hexlet.code.reporsitory.UserRepository;
import hexlet.code.util.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static hexlet.code.util.TestUtils.USER_CONTROLLER_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @BeforeEach
    void init() throws Exception {
        // create new user
        utils.createUser(TestUtils.USER_DTO_1);
        utils.createUser(TestUtils.USER_DTO_2);
    }

    @AfterEach
    void clear() {
        utils.tearDown();
    }

    @Nested
    class UnprotectedRoutesChecks {
        @Test
        void getUser() throws Exception {
            User user = utils.getUserByEmail(TestUtils.TEST_EMAIL_1);
            MockHttpServletResponse response = mockMvc.perform(
                    get(USER_CONTROLLER_PATH + "/" + user.getId())
            ).andReturn().getResponse();
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getContentAsString()).contains(user.getEmail());
            assertThat(response.getContentAsString()).contains(user.getFirstName());
            assertThat(response.getContentAsString()).contains(user.getLastName());
        }

        @Test
        void getUsers() throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
                    get(USER_CONTROLLER_PATH)
            ).andReturn().getResponse();

            List<User> userList = userRepository.findAll();
            List<User> userListResponse = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() { });

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(userListResponse).containsAll(userList);
        }
    }

    @Nested
    class UpdateChecks {
        @Test
        void updateUser() throws Exception {
            // find created user by email and store his password hash
            User userBeforeUpdate = utils.getUserByEmail(TestUtils.TEST_EMAIL_1);
            String passwordBeforeUpdate = utils.getUserByEmail(TestUtils.TEST_EMAIL_1).getPassword();
            UserDTO userUpdateDto = new UserDTO(
                    "updated_email@mail.com",
                    "firstname",
                    "lastname",
                    "updated_password"
            );

            MockHttpServletResponse updateResponse = mockMvc.perform(
                    put(USER_CONTROLLER_PATH + "/" + userBeforeUpdate.getId())
                            .header("Authorization", "Bearer "
                                    + utils.getJwtToken(TestUtils.TEST_EMAIL_1, TestUtils.TEST_PASSWORD_1))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(MAPPER.writeValueAsString(userUpdateDto))
            ).andReturn().getResponse();

            // checking whether the user's data has been updated
            User userAfterUpdate = utils.getUserByEmail(userUpdateDto.getEmail());
            System.out.println("UPDATE RESPONSE: " + updateResponse.getContentAsString());
            assertThat(updateResponse.getStatus()).isEqualTo(200);
            assertThat(userAfterUpdate.getEmail()).isEqualTo(userUpdateDto.getEmail());
            assertThat(userAfterUpdate.getFirstName()).isEqualTo(userUpdateDto.getFirstName());
            assertThat(userAfterUpdate.getLastName()).isEqualTo(userUpdateDto.getLastName());
            // making sure that the password has been changed
            assertThat(userAfterUpdate.getPassword()).isNotEqualTo(passwordBeforeUpdate);
        }

        @Test
        void updateUserFail() throws Exception {
            User user = utils.getUserByEmail(TestUtils.TEST_EMAIL_1);
            UserDTO userUpdateDto = TestUtils.USER_DTO_2;

            MockHttpServletResponse updateResponse = mockMvc.perform(
                    put(USER_CONTROLLER_PATH + "/" + user.getId())
                            .header("Authorization", "Bearer "
                                    + utils.getJwtToken(TestUtils.TEST_EMAIL_2, TestUtils.TEST_PASSWORD_2))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(MAPPER.writeValueAsString(userUpdateDto))
            ).andReturn().getResponse();
            // making sure that user can't remove another user
            assertThat(updateResponse.getStatus()).isEqualTo(403);
        }
    }

    @Nested
    class DeleteChecks {
        @Test
        void deleteUserOwnRecord() throws Exception {
            // find created user by email
            User user = utils.getUserByEmail(TestUtils.TEST_EMAIL_1);
            assertThat(user).isNotNull();

            // try to authenticate
            mockMvc.perform(
                    delete(USER_CONTROLLER_PATH + "/" + user.getId())
                            .header("Authorization", "Bearer "
                                    + utils.getJwtToken(TestUtils.TEST_EMAIL_1, TestUtils.TEST_PASSWORD_1))
            ).andExpect(status().isOk());

            NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                    () -> utils.getUserByEmail(TestUtils.TEST_EMAIL_1), "No value present");
            assertThat(exception.getMessage()).isEqualTo("No value present");
        }

        @Test
        void deleteSomeoneElseUser() throws Exception {
            User user = utils.getUserByEmail(TestUtils.TEST_EMAIL_2);
            MockHttpServletResponse response = mockMvc.perform(
                    delete(USER_CONTROLLER_PATH + "/" + user.getId())
                            .header("Authorization", "Bearer "
                                    + utils.getJwtToken(TestUtils.TEST_EMAIL_1, TestUtils.TEST_PASSWORD_1))
            ).andReturn().getResponse();
            assertThat(utils.getUserByEmail(TestUtils.TEST_EMAIL_2)).isNotNull();
            assertThat(response.getStatus()).isEqualTo(403);
        }
    }
}
