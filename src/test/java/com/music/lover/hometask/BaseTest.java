package com.music.lover.hometask;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper mapper;

    protected MockMvc getMockMvc() {
        return mockMvc;
    }

}
