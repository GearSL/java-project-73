package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.JwtRequestDTO;
import hexlet.code.dto.JwtResponseDTO;
import hexlet.code.model.User;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StatusControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtils utils;
    private static final String BASE_URL = "/api";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @AfterEach
    void clear() {
        utils.tearDown();
    }

    @BeforeEach
    void init() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(TestUtils.STATUS_CONTROLLER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(utils.getTestStatusCreationDTO()))
        ).andExpect(status().isOk());

        // create new users
         mockMvc.perform(MockMvcRequestBuilders.post(TestUtils.USER_CONTROLLER_PATH)
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(MAPPER.writeValueAsString(utils.getTestUserCreationDto(TestUtils.TEST_EMAIL)))
         ).andExpect(status().isOk());
    }

    @Nested
    class UnauthorizedRoutesCheck {
        @Test
        void getAllStatuses() throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
                    MockMvcRequestBuilders.get(TestUtils.STATUS_CONTROLLER_PATH)
            ).andReturn().getResponse();
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getContentAsString()).contains(TestUtils.TEST_STATUS);
        }

        @Test
        void getStatusById() throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
                    MockMvcRequestBuilders.get(TestUtils.STATUS_CONTROLLER_PATH + "/" + utils.getExactStatusId())
            ).andReturn().getResponse();
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getContentAsString()).contains(TestUtils.TEST_STATUS);
        }
    }

    @Nested
    class AuthorizedRoutesCheck {
        @Test
        void putStatusById() throws Exception {
            // find created user by email
            User user = utils.getUserByEmail(TestUtils.TEST_EMAIL);
            assertThat(user).isNotNull();

            // try to authenticate
            JwtRequestDTO requestDTO = new JwtRequestDTO(TestUtils.TEST_EMAIL, TestUtils.TEST_PASSWORD);
            MockHttpServletResponse response = mockMvc.perform(
                    MockMvcRequestBuilders.post(BASE_URL + "/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(MAPPER.writeValueAsBytes(requestDTO))
            ).andReturn().getResponse();
            assertThat(response.getStatus()).isEqualTo(200);

            // try to update status
            JwtResponseDTO responseDTO = MAPPER.readValue(response.getContentAsString(), JwtResponseDTO.class);
            MockHttpServletResponse putResponse = mockMvc.perform(
                    MockMvcRequestBuilders.put(TestUtils.STATUS_CONTROLLER_PATH + "/" + utils.getExactStatusId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer "
                                    + responseDTO.getToken())
                            .content(MAPPER.writeValueAsString(utils.getTestStatusUpdatingDTO()))
            ).andReturn().getResponse();
            assertThat(putResponse.getStatus()).isEqualTo(200);
            assertThat(putResponse.getContentAsString()).contains(TestUtils.TEST_STATUS_2);
        }

        @Test
        void putStatusByIdFailed() throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
                    MockMvcRequestBuilders.put(TestUtils.STATUS_CONTROLLER_PATH + "/" + utils.getExactStatusId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(MAPPER.writeValueAsString(utils.getTestStatusUpdatingDTO()))
            ).andReturn().getResponse();
            assertThat(response.getStatus()).isEqualTo(403);
        }
    }
}
