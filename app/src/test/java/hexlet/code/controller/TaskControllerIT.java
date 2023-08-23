package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.model.Task;
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

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtils utils;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    private static final ObjectMapper MAPPER = new ObjectMapper();


    @BeforeEach
    void init() throws Exception {
        // create new users
        utils.createUser(TestUtils.USER_DTO_1);
        utils.createUser(TestUtils.USER_DTO_2);
        // create new status
        utils.createStatus(TestUtils.TASK_STATUS_DTO_1);
        utils.createStatus(TestUtils.TASK_STATUS_DTO_2);
        // create new task
        Long userId = utils.getUserByEmail(TestUtils.USER_DTO_1.getEmail()).getId();
        Long statusId = utils.getStatusId();
        TaskDTO taskDTO = new TaskDTO(
                TestUtils.TASK_NAME,
                TestUtils.TASK_DESCRIPTION,
                userId,
                userId,
                statusId
        );
        utils.createTask(taskDTO);
    }

    @AfterEach
    void clear() {
        utils.tearDown();
    }

    @Nested
    class UnauthorizedRoutesCheck {
        @Test
        void getTaskById() throws Exception {
            Optional<Task> task = utils.findByName(TestUtils.TASK_NAME);
            MockHttpServletResponse response = mockMvc.perform(
                    get(TestUtils.BASE_URL + "/tasks/" + task.get().getId())
                            .contentType(MediaType.APPLICATION_JSON)
            ).andReturn().getResponse();
        }

        @Test
        void getTaskList() throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
                    get(TestUtils.BASE_URL + "/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk()).andReturn().getResponse();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getContentAsString()).isNotBlank();
            assertThat(response.getContentAsString()).contains(TestUtils.TASK_NAME);
        }
    }

    @Nested
    class AuthorizedRoutesCheck {
        @Test
        void successUpdateTask() throws Exception {
            Optional<Task> task = utils.findByName(TestUtils.TASK_NAME);
            Long userId = utils.getUserByEmail(TestUtils.USER_DTO_1.getEmail()).getId();
            Long statusId = utils.getStatusId();
            TaskUpdateDTO updateDTO = new TaskUpdateDTO(
                    "updated name",
                    "updated description",
                    userId,
                    statusId
            );
            MockHttpServletResponse response = mockMvc.perform(
                    put(TestUtils.BASE_URL + "/tasks/" + task.get().getId())
                            .header("Authorization", "Bearer "
                                    + utils.getJwtToken(TestUtils.TEST_EMAIL_1, TestUtils.TEST_PASSWORD_1))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(MAPPER.writeValueAsString(updateDTO))
            ).andReturn().getResponse();

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getContentAsString()).isNotBlank();
            assertThat(response.getContentAsString()).contains("updated name");
            assertThat(response.getContentAsString()).contains("updated description");
        }

        @Test
        void failedUpdateTask() throws Exception {
            Optional<Task> task = utils.findByName(TestUtils.TASK_NAME);
            Long userId = utils.getUserByEmail(TestUtils.USER_DTO_1.getEmail()).getId();
            Long statusId = utils.getStatusId();
            TaskUpdateDTO updateDTO = new TaskUpdateDTO(
                    "updated name",
                    "updated description",
                    userId,
                    statusId
            );
            MockHttpServletResponse response = mockMvc.perform(
                    put(TestUtils.BASE_URL + "/tasks/" + task.get().getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(MAPPER.writeValueAsString(updateDTO))
            ).andReturn().getResponse();
            assertThat(response.getStatus()).isEqualTo(403);
        }
    }
}
