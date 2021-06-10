package com.music.lover.hometask.controller;

import com.music.lover.hometask.BaseControllerTest;
import com.music.lover.hometask.data.UserMock;
import com.music.lover.hometask.dto.UserLoginResponse;
import com.music.lover.hometask.dto.UserRegistrationRequest;
import com.music.lover.hometask.dto.UserRegistrationResponse;
import com.music.lover.hometask.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserControllerIntegrationTest extends BaseControllerTest {

    @MockBean
    private UserRepository userRepository;

    @Test
    void testRegisterUser() throws Exception {
        UserRegistrationRequest registrationDTO = UserMock.getUserRegistrationDTO();

        when(userRepository.existsByName(any()))
                .thenAnswer(invocation -> false);

        when(userRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MvcResult mvcResult = getMockMvc()
                .perform(
                        MockMvcRequestBuilders.post("/v1/users/register")
                                .with(httpBasic("user", "pass"))
                                .content(mapper.writeValueAsString(registrationDTO))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andDo(documentRegisterUser())
                .andDo(print())
                .andReturn();

        verify(userRepository, times(1))
                .existsByName(any());

        verify(userRepository, times(1))
                .save(any());

        String responseString = mvcResult.getResponse().getContentAsString();
        UserRegistrationResponse responseDTO = mapper.readValue(responseString, UserRegistrationResponse.class);

        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(registrationDTO.getName(), responseDTO.getName());
        Assertions.assertEquals(registrationDTO.getUsername(), responseDTO.getUsername());
        Assertions.assertEquals(registrationDTO.getPassword(), responseDTO.getPassword());
        Assertions.assertEquals(registrationDTO.getRepeatPassword(), responseDTO.getPassword());
        Assertions.assertFalse(responseDTO.getUuid().isEmpty());
    }

    @Test
    void testLoginUser() throws Exception {
        when(userRepository.findByUsernameAndPassword(any(), any()))
                .thenAnswer(invocation -> Optional.of(UserMock.getUser()));

        MvcResult mvcResult = getMockMvc()
                .perform(
                        MockMvcRequestBuilders.post("/v1/users/login")
                                .with(httpBasic("user", "pass"))
                                .content(mapper.writeValueAsString(UserMock.getUserLoginDTO()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(documentLoginUser())
                .andDo(print())
                .andReturn();

        verify(userRepository, times(1))
                .findByUsernameAndPassword(any(), any());

        String responseString = mvcResult.getResponse().getContentAsString();
        UserLoginResponse responseDTO = mapper.readValue(responseString, UserLoginResponse.class);

        Assertions.assertNotNull(responseDTO);
        Assertions.assertFalse(responseDTO.getUuid().isEmpty());
    }

    private RestDocumentationResultHandler documentRegisterUser() {
        return document(
                "registerUser",
                preprocessRequest(removeHeaders("Content-Length", "Host"), prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName("Authorization").description("Basic authentication header")
                ),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("User name (that will be shown to others)"),
                        fieldWithPath("username").type(JsonFieldType.STRING).description("User authentication username"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("User authentication password"),
                        fieldWithPath("repeatPassword").type(JsonFieldType.STRING).description("User authentication password")
                ),
                responseFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("User name (that will be shown to others)"),
                        fieldWithPath("username").type(JsonFieldType.STRING).description("User authentication username"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("User authentication password"),
                        fieldWithPath("uuid").type(JsonFieldType.STRING).description("User unique id")
                )
        );
    }

    private RestDocumentationResultHandler documentLoginUser() {
        return document(
                "loginUser",
                preprocessRequest(removeHeaders("Content-Length", "Host"), prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName("Authorization").description("Basic authentication header")
                ),
                requestFields(
                        fieldWithPath("username").type(JsonFieldType.STRING).description("User authentication username"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("User authentication password")
                ),
                responseFields(
                        fieldWithPath("uuid").type(JsonFieldType.STRING).description("User unique id")
                )
        );
    }

}
