package com.music.lover.hometask.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.music.lover.hometask.dto.UserLoginDTO;
import com.music.lover.hometask.dto.UserLoginResponseDTO;
import com.music.lover.hometask.dto.UserRegistrationDTO;
import com.music.lover.hometask.dto.UserRegistrationResponseDTO;
import com.music.lover.hometask.exception.PasswordsDontMatchException;
import com.music.lover.hometask.exception.UserAlreadyExistsException;
import com.music.lover.hometask.exception.UserNotFoundException;
import com.music.lover.hometask.exception.error.ApplicationError;
import com.music.lover.hometask.exception.error.RequestError;
import com.music.lover.hometask.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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

@WebMvcTest(UserController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Test
    void testRegisterUser() throws Exception {
        UserRegistrationDTO userRegistrationDTO = getUserRegistrationDTO();

        when(userService.registerUser(any()))
                .thenAnswer(invocation -> new UserRegistrationResponseDTO(
                                "TestName",
                                "TestUsername",
                                "TestPassword",
                                UUID.randomUUID().toString()
                        )
                );

        MvcResult mvcResult = mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/v1/users/register")
                                .with(httpBasic("user", "pass"))
                                .content(mapper.writeValueAsString(userRegistrationDTO))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andDo(
                        document(
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
                        )
                )
                .andDo(print())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        UserRegistrationResponseDTO responseDTO = mapper.readValue(responseString, UserRegistrationResponseDTO.class);

        Assertions.assertNotNull(responseDTO);
        Assertions.assertFalse(responseDTO.getName().isEmpty());
        Assertions.assertFalse(responseDTO.getUsername().isEmpty());
        Assertions.assertFalse(responseDTO.getPassword().isEmpty());
        Assertions.assertFalse(responseDTO.getUuid().isEmpty());
    }

    @Test
    void testRegisterUserPasswordsDontMatchException() throws Exception {
        UserRegistrationDTO userRegistrationDTO = getUserRegistrationDTO();

        when(userService.registerUser(any()))
                .thenThrow(new PasswordsDontMatchException());

        MvcResult mvcResult = mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/v1/users/register")
                                .with(httpBasic("user", "pass"))
                                .content(mapper.writeValueAsString(userRegistrationDTO))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();

        assertRequestError(responseString, ApplicationError.PASSWORDS_DONT_MATCH);
    }

    @Test
    void testRegisterUserUserAlreadyExistsException() throws Exception {
        UserRegistrationDTO userRegistrationDTO = getUserRegistrationDTO();

        when(userService.registerUser(any()))
                .thenThrow(new UserAlreadyExistsException());

        MvcResult mvcResult = mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/v1/users/register")
                                .with(httpBasic("user", "pass"))
                                .content(mapper.writeValueAsString(userRegistrationDTO))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();

        assertRequestError(responseString, ApplicationError.USER_ALREADY_EXISTS);
    }

    @Test
    void testLoginUser() throws Exception {
        UserLoginDTO userLoginDTO = getUserLoginDTO();

        when(userService.loginUser(any()))
                .thenAnswer(invocation -> new UserLoginResponseDTO(
                                UUID.randomUUID().toString()
                        )
                );

        MvcResult mvcResult = mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/v1/users/login")
                                .with(httpBasic("user", "pass"))
                                .content(mapper.writeValueAsString(userLoginDTO))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(
                        document(
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
                        )
                )
                .andDo(print())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        UserLoginResponseDTO responseDTO = mapper.readValue(responseString, UserLoginResponseDTO.class);

        Assertions.assertNotNull(responseDTO);
        Assertions.assertFalse(responseDTO.getUuid().isEmpty());
    }

    @Test
    void testLoginUserUserNotFoundException() throws Exception {
        UserLoginDTO userLoginDTO = getUserLoginDTO();

        when(userService.loginUser(any()))
                .thenThrow(new UserNotFoundException());

        MvcResult mvcResult = mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/v1/users/login")
                                .with(httpBasic("user", "pass"))
                                .content(mapper.writeValueAsString(userLoginDTO))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();

        assertRequestError(responseString, ApplicationError.USER_NOT_FOUND);
    }

    private void assertRequestError(String response, ApplicationError applicationError) throws JsonProcessingException {
        RequestError error = mapper.readValue(response, RequestError.class);

        Assertions.assertEquals(applicationError.getErrorName(), error.getErrorName());
        Assertions.assertEquals(applicationError.getHttpStatus().toString(), error.getHttpStatus());
    }

    private UserRegistrationDTO getUserRegistrationDTO() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setName("TestName");
        userRegistrationDTO.setUsername("TestUsername");
        userRegistrationDTO.setPassword("TestPassword");
        userRegistrationDTO.setRepeatPassword("TestPassword");

        return userRegistrationDTO;
    }

    private UserLoginDTO getUserLoginDTO() {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername("TestUsername");
        userLoginDTO.setPassword("TestPassword");

        return userLoginDTO;
    }

}
