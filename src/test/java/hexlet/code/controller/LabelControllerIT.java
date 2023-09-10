package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.LabelDTO;
import hexlet.code.model.Label;
import hexlet.code.reporsitory.LabelRepository;
import hexlet.code.util.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class LabelControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtils utils;
    @Autowired
    private LabelRepository labelRepository;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @BeforeEach
    void init() throws Exception {
        // create new users
        utils.createUser(TestUtils.USER_DTO_1);
        //create new labels
        utils.createLabel(TestUtils.LABEL_DTO_1);
        utils.createLabel(TestUtils.LABEL_DTO_2);
    }

    @AfterEach
    void clear() {
        utils.tearDown();
    }

    @Test
    void getAllLabels() throws Exception {
        List<Label> labelList = labelRepository.findAll();
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(TestUtils.LABEL_CONTROLLER_PATH)
                        .header("Authorization", "Bearer "
                                + utils.getJwtToken(TestUtils.TEST_EMAIL_1, TestUtils.TEST_PASSWORD_1))
        ).andReturn().getResponse();

        System.out.println(response.getContentAsString());

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(labelRepository.findAll().isEmpty()).isFalse();
        assertThat(response.getContentAsString()).contains(TestUtils.LABEL_NAME_1);
        assertThat(response.getContentAsString()).contains(TestUtils.LABEL_NAME_2);
        assertThat(response.getContentAsString()).contains(labelList.get(0).getName());
        assertThat(response.getContentAsString()).contains(labelList.get(1).getName());
    }

    @Test
    void getLabelById() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(TestUtils.LABEL_CONTROLLER_PATH + "/"
                                + utils.getLabelId(TestUtils.LABEL_NAME_1))
                        .header("Authorization", "Bearer "
                                + utils.getJwtToken(TestUtils.TEST_EMAIL_1, TestUtils.TEST_PASSWORD_1))
        ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains(TestUtils.LABEL_NAME_1);
    }

    @Test
    void successUpdateLabel() throws Exception {
        LabelDTO labelUpdateDto = new LabelDTO("Updated");

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put(TestUtils.LABEL_CONTROLLER_PATH + "/"
                                + utils.getLabelId(TestUtils.LABEL_NAME_1))
                        .header("Authorization", "Bearer "
                                + utils.getJwtToken(TestUtils.TEST_EMAIL_1, TestUtils.TEST_PASSWORD_1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(labelUpdateDto))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains(labelUpdateDto.getName());
    }

    @Test
    void failedUpdateLabel() throws Exception {
        LabelDTO labelUpdateDto = new LabelDTO("Updated");

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.put(TestUtils.LABEL_CONTROLLER_PATH + "/"
                                + utils.getLabelId(TestUtils.LABEL_NAME_1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsString(labelUpdateDto))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void successDeleteLabel() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete(TestUtils.LABEL_CONTROLLER_PATH + "/"
                                + utils.getLabelId(TestUtils.LABEL_NAME_1))
                        .header("Authorization", "Bearer "
                                + utils.getJwtToken(TestUtils.TEST_EMAIL_1, TestUtils.TEST_PASSWORD_1))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(labelRepository.findByName(TestUtils.LABEL_NAME_1).isEmpty()).isTrue();
    }

    @Test
    void failedDeleteLabel() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete(TestUtils.LABEL_CONTROLLER_PATH + "/"
                                + utils.getLabelId(TestUtils.LABEL_NAME_1))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(403);
    }
}
