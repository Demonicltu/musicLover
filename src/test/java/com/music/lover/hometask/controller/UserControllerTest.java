package com.music.lover.hometask.controller;

import com.music.lover.hometask.BaseControllerTest;
import com.music.lover.hometask.data.UserMock;
import com.music.lover.hometask.dto.UserLoginResponse;
import com.music.lover.hometask.dto.UserRegistrationResponse;
import com.music.lover.hometask.exception.PasswordsDontMatchException;
import com.music.lover.hometask.exception.UserAlreadyExistsException;
import com.music.lover.hometask.exception.UserNotFoundException;
import com.music.lover.hometask.exception.error.ApplicationError;
import com.music.lover.hometask.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserControllerTest extends BaseControllerTest {

    @MockBean
    private UserService userService;

    @Test
    void testRegisterUser() throws Exception {
        when(userService.registerUser(any()))
                .thenAnswer(invocation -> UserMock.getUserRegistrationResponse());

        MvcResult mvcResult = getMockMvc()
                .perform(
                        MockMvcRequestBuilders.post("/v1/users/register")
                                .with(httpBasic("user", "pass"))
                                .content(mapper.writeValueAsString(UserMock.getUserRegistrationDTO()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        UserRegistrationResponse responseDTO = mapper.readValue(responseString, UserRegistrationResponse.class);

        Assertions.assertNotNull(responseDTO);
        Assertions.assertFalse(responseDTO.getName().isEmpty());
        Assertions.assertFalse(responseDTO.getUsername().isEmpty());
        Assertions.assertFalse(responseDTO.getPassword().isEmpty());
        Assertions.assertFalse(responseDTO.getUuid().isEmpty());
    }

    @Test
    void testRegisterUserPasswordsDontMatchException() throws Exception {
        when(userService.registerUser(any()))
                .thenThrow(new PasswordsDontMatchException());

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/v1/users/register")
                .with(httpBasic("user", "pass"))
                .content(mapper.writeValueAsString(UserMock.getUserRegistrationDTO()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        testException(status().isBadRequest(), ApplicationError.PASSWORDS_DONT_MATCH, mockHttpServletRequestBuilder);
    }

    @Test
    void testRegisterUserUserAlreadyExistsException() throws Exception {
        when(userService.registerUser(any()))
                .thenThrow(new UserAlreadyExistsException());

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/v1/users/register")
                .with(httpBasic("user", "pass"))
                .content(mapper.writeValueAsString(UserMock.getUserRegistrationDTO()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        testException(status().isBadRequest(), ApplicationError.USER_ALREADY_EXISTS, mockHttpServletRequestBuilder);
    }

    @Test
    void testLoginUser() throws Exception {
        when(userService.loginUser(any()))
                .thenAnswer(invocation -> new UserLoginResponse(UUID.randomUUID().toString())
                );

        MvcResult mvcResult = getMockMvc()
                .perform(
                        MockMvcRequestBuilders.post("/v1/users/login")
                                .with(httpBasic("user", "pass"))
                                .content(mapper.writeValueAsString(UserMock.getUserLoginDTO()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        UserLoginResponse responseDTO = mapper.readValue(responseString, UserLoginResponse.class);

        Assertions.assertNotNull(responseDTO);
        Assertions.assertFalse(responseDTO.getUuid().isEmpty());
    }

    @Test
    void testLoginUserUserNotFoundException() throws Exception {
        when(userService.loginUser(any()))
                .thenThrow(new UserNotFoundException());

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/v1/users/login")
                .with(httpBasic("user", "pass"))
                .content(mapper.writeValueAsString(UserMock.getUserLoginDTO()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        testException(status().isNotFound(), ApplicationError.USER_NOT_FOUND, mockHttpServletRequestBuilder);
    }

}
