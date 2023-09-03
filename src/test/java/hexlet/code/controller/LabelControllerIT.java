package hexlet.code.controller;

import hexlet.code.util.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class LabelControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtils utils;

    @BeforeEach
    void init() throws Exception {
        // create new users
        utils.createUser(TestUtils.USER_DTO_1);

        utils.createLabel(TestUtils.LABEL_DTO_1);
        utils.createLabel(TestUtils.LABEL_DTO_2);
    }

    @AfterEach
    void clear() {
        utils.tearDown();
    }

    @Test
    void getAllLabels() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get(TestUtils.LABEL_CONTROLLER_PATH)
                        .header("Authorization", "Bearer "
                                + utils.getJwtToken(TestUtils.TEST_EMAIL_1, TestUtils.TEST_PASSWORD_1))
        ).andReturn().getResponse();

        System.out.println(response.getContentAsString());

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains(TestUtils.LABEL_NAME_1);
        assertThat(response.getContentAsString()).contains(TestUtils.LABEL_NAME_2);
    }

    @Test
    void getLabelById() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get(TestUtils.LABEL_CONTROLLER_PATH + "/" + utils.getLabelId())
                        .header("Authorization", "Bearer "
                                + utils.getJwtToken(TestUtils.TEST_EMAIL_1, TestUtils.TEST_PASSWORD_1))
        ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains(TestUtils.LABEL_NAME_1);
    }

    //TODO: add tests for other routes and for unauthorized requests
}
