package com.music.lover.hometask.controller;

import com.music.lover.hometask.BaseControllerTest;
import com.music.lover.hometask.constant.ApiHttpHeaders;
import com.music.lover.hometask.data.ArtistMock;
import com.music.lover.hometask.dto.ArtistResponse;
import com.music.lover.hometask.exception.ArtistAlreadyExistsException;
import com.music.lover.hometask.exception.ServiceException;
import com.music.lover.hometask.exception.UriBuildException;
import com.music.lover.hometask.exception.error.ApplicationError;
import com.music.lover.hometask.service.ArtistService;
import com.music.lover.hometask.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ArtistControllerTest extends BaseControllerTest {

    @MockBean
    private ArtistService artistService;

    @MockBean
    private UserService userService;

    @Test
    void testGetArtist() throws Exception {
        when(artistService.getArtist(any()))
                .thenAnswer(invocation -> Collections.singletonList(ArtistMock.getArtistResponse())
                );

        MvcResult mvcResult = getMockMvc()
                .perform(
                        MockMvcRequestBuilders.get("/v1/artists")
                                .with(httpBasic("user", "pass"))
                                .header(ApiHttpHeaders.USER_KEY_HEADER.getHeaderValue(), "testUUID")
                                .param("artist", "testArtist")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        List<ArtistResponse> artistResponseList = Arrays.asList(mapper.readValue(responseString, ArtistResponse[].class));

        Assertions.assertFalse(artistResponseList.isEmpty());
        Assertions.assertNotNull(artistResponseList.get(0).getAmgArtistId());
        Assertions.assertFalse(artistResponseList.get(0).getArtistName().isEmpty());
    }

    @Test
    void testGetArtistServiceException() throws Exception {
        when(artistService.getArtist(any()))
                .thenThrow(new ServiceException("Test exception"));

        testGetArtistException(status().isServiceUnavailable(), ApplicationError.SERVICE_UNAVAILABLE);
    }


    @Test
    void testGetArtistUirBuildException() throws Exception {
        when(artistService.getArtist(any()))
                .thenThrow(new UriBuildException(new Exception("Test exception")));

        testGetArtistException(status().isInternalServerError(), ApplicationError.SYS_ERR);
    }

    @Test
    void testSaveArtist() throws Exception {
        when(artistService.saveArtist(any(), any()))
                .thenAnswer(invocation -> ArtistMock.getArtistResponse());

        MvcResult mvcResult = getMockMvc()
                .perform(
                        MockMvcRequestBuilders.post("/v1/artists")
                                .with(httpBasic("user", "pass"))
                                .header(ApiHttpHeaders.USER_KEY_HEADER.getHeaderValue(), "testUUID")
                                .content(mapper.writeValueAsString(ArtistMock.getNewArtistDTO()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        ArtistResponse artistResponse = mapper.readValue(responseString, ArtistResponse.class);

        Assertions.assertNotNull(artistResponse);
        Assertions.assertNotNull(artistResponse.getAmgArtistId());
        Assertions.assertFalse(artistResponse.getArtistName().isEmpty());
    }

    @Test
    void testSaveArtistAlreadyExists() throws Exception {
        when(artistService.saveArtist(any(), any()))
                .thenThrow(new ArtistAlreadyExistsException());

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/v1/artists")
                .with(httpBasic("user", "pass"))
                .header(ApiHttpHeaders.USER_KEY_HEADER.getHeaderValue(), "testUUID")
                .content(mapper.writeValueAsString(ArtistMock.getNewArtistDTO()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        testException(status().isBadRequest(), ApplicationError.ARTIST_ALREADY_SAVED, mockHttpServletRequestBuilder);
    }

    private void testGetArtistException(ResultMatcher status, ApplicationError error) throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/v1/artists")
                .with(httpBasic("user", "pass"))
                .header(ApiHttpHeaders.USER_KEY_HEADER.getHeaderValue(), "testUUID")
                .param("artist", "testArtist")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        testException(status, error, mockHttpServletRequestBuilder);
    }

}
