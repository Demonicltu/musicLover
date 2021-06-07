package com.music.lover.hometask.controller;

import com.music.lover.hometask.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class HomeControllerTest extends BaseTest {

    @Test
    void testHomePageEndpoint() throws Exception {
        MvcResult mvcResult = getMockMvc()
                .perform(
                        MockMvcRequestBuilders.get("/")
                                .with(httpBasic("user", "pass"))
                )
                .andExpect(status().isOk())
                .andDo(
                        document(
                                "home",
                                preprocessRequest(removeHeaders("Host"), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("Basic authentication header")
                                )
                        )
                )
                .andDo(print())
                .andReturn();

        Assertions.assertEquals("API is up!", mvcResult.getResponse().getContentAsString());
    }
}
