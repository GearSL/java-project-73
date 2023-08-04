package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StatusControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtils utils;
    private static final String BASE_URL = "/api";
    private static final String STATUS_CONTROLLER_PATH = BASE_URL + StatusController.STATUS_CONTROLLER_PATH;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @BeforeEach
    void init() throws Exception {
        mockMvc.perform(
                post(STATUS_CONTROLLER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(utils.getTestStatusCreationDTO()))
        ).andExpect(status().isOk());

        // create new users
//        utils.createUser(TestUtils.TEST_EMAIL);
//        utils.createUser(TestUtils.TEST_EMAIL_2);
    }

    @AfterEach
    void clear() {
        utils.tearDown();
    }

    @Nested
    class UnauthorizedRoutesCheck {
        @Test
        void getAllStatuses() throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
                    get(STATUS_CONTROLLER_PATH)
            ).andReturn().getResponse();
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getContentAsString()).contains(TestUtils.TEST_STATUS);
        }

        @Test
        void getStatusById() throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
                    get(STATUS_CONTROLLER_PATH + "/" + utils.getExactStatusId())
            ).andReturn().getResponse();
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getContentAsString()).contains(TestUtils.TEST_STATUS);
        }
    }

    @Nested
    class authorizedRoutesCheck {
//        @Test
//        void putStatusById() throws Exception {
//            MockHttpServletResponse response = mockMvc.perform(
//                    put(STATUS_CONTROLLER_PATH + "/" + utils.getExactStatusId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .header("Authorization", "Bearer "
//                                    + utils.getTokenForUser(TestUtils.TEST_EMAIL, TestUtils.TEST_PASSWORD))
//                            .content(MAPPER.writeValueAsString(utils.getTestStatusUpdatingDTO()))
//            ).andReturn().getResponse();
//            assertThat(response.getStatus()).isEqualTo(200);
//            assertThat(response.getContentAsString()).contains(TestUtils.TEST_STATUS_2);
//        }

        @Test
        void putStatusByIdFailed() throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
                    put(STATUS_CONTROLLER_PATH + "/" + utils.getExactStatusId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(MAPPER.writeValueAsString(utils.getTestStatusUpdatingDTO()))
            ).andReturn().getResponse();
            assertThat(response.getStatus()).isEqualTo(403);
        }
    }
}
