package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.reporsitory.TaskStatusRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskStatusControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtils utils;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    private static final String BASE_URL = "/api";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @BeforeEach
    void init() throws Exception {
        // create new statuses
        utils.createStatus(TestUtils.TASK_STATUS_DTO_1);
        // create new users
        utils.createUser(TestUtils.USER_DTO_1);
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
                    get(TestUtils.STATUS_CONTROLLER_PATH)
                            .header("Authorization", "Bearer "
                                    + utils.getJwtToken(TestUtils.TEST_EMAIL_1, TestUtils.TEST_PASSWORD_1))
            ).andReturn().getResponse();

            List<TaskStatus> taskStatusList = taskStatusRepository.findAll();
            List<TaskStatus> responseTaskStatusList = TestUtils.fromJson(response.getContentAsString(),
                    new TypeReference<>() { });

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getContentAsString()).contains(TestUtils.TEST_STATUS_1);
            assertThat(taskStatusList).containsAll(responseTaskStatusList);
        }

        @Test
        void getStatusById() throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
                    get(TestUtils.STATUS_CONTROLLER_PATH + "/" + utils.getStatusId())
                            .header("Authorization", "Bearer "
                                    + utils.getJwtToken(TestUtils.TEST_EMAIL_1, TestUtils.TEST_PASSWORD_1))
            ).andReturn().getResponse();
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getContentAsString()).contains(TestUtils.TEST_STATUS_1);
        }
    }

    @Nested
    class AuthorizedRoutesCheck {
        @Test
        void putStatusById() throws Exception {
            MockHttpServletResponse putResponse = mockMvc.perform(
                    put(TestUtils.STATUS_CONTROLLER_PATH + "/" + utils.getStatusId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer "
                                    + utils.getJwtToken(TestUtils.TEST_EMAIL_1, TestUtils.TEST_PASSWORD_1))
                            .content(MAPPER.writeValueAsString(TestUtils.TASK_STATUS_DTO_2))
            ).andReturn().getResponse();
            assertThat(putResponse.getStatus()).isEqualTo(200);
            assertThat(putResponse.getContentAsString()).contains(TestUtils.TEST_STATUS_2);
        }

        @Test
        void putStatusByIdFailed() throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
                    put(TestUtils.STATUS_CONTROLLER_PATH + "/" + utils.getStatusId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(MAPPER.writeValueAsString(TestUtils.TASK_STATUS_DTO_2))
            ).andReturn().getResponse();
            assertThat(response.getStatus()).isEqualTo(403);
        }
    }
}
