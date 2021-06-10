package com.music.lover.hometask.controller;

import com.music.lover.hometask.BaseControllerTest;
import com.music.lover.hometask.constant.ApiHttpHeaders;
import com.music.lover.hometask.data.AlbumMock;
import com.music.lover.hometask.dto.AlbumResponse;
import com.music.lover.hometask.exception.ServiceException;
import com.music.lover.hometask.exception.UriBuildException;
import com.music.lover.hometask.exception.error.ApplicationError;
import com.music.lover.hometask.service.AlbumService;
import com.music.lover.hometask.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
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
class AlbumControllerTest extends BaseControllerTest {

    @MockBean
    private AlbumService albumService;

    @MockBean
    private UserService userService;

    @Test
    void testGetAlbums() throws Exception {
        when(albumService.getAlbums(any()))
                .thenAnswer(invocation -> Collections.singletonList(AlbumMock.getAlbumResponse())
                );

        MvcResult mvcResult = getMockMvc()
                .perform(
                        RestDocumentationRequestBuilders.get("/v1/artists/{amgArtistId}/albums", 15)
                                .with(httpBasic("user", "pass"))
                                .header(ApiHttpHeaders.USER_KEY_HEADER.getHeaderValue(), "testUUID")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        List<AlbumResponse> albumResponseList = Arrays.asList(mapper.readValue(responseString, AlbumResponse[].class));

        Assertions.assertFalse(albumResponseList.isEmpty());
        Assertions.assertFalse(albumResponseList.get(0).getArtistResponse().getArtistName().isEmpty());
        Assertions.assertNotNull(albumResponseList.get(0).getArtistResponse().getAmgArtistId());
        Assertions.assertTrue(albumResponseList.get(0).getCollectionPrice() > 0);
        Assertions.assertFalse(albumResponseList.get(0).getCurrency().isEmpty());
        Assertions.assertTrue(albumResponseList.get(0).getTrackCount() > 0);
        Assertions.assertFalse(albumResponseList.get(0).getCopyright().isEmpty());
        Assertions.assertFalse(albumResponseList.get(0).getCountry().isEmpty());
        Assertions.assertFalse(albumResponseList.get(0).getPrimaryGenreName().isEmpty());
        Assertions.assertNotNull(albumResponseList.get(0).getReleaseDate());
    }

    @Test
    void testGetAlbumsServiceException() throws Exception {
        when(albumService.getAlbums(any()))
                .thenThrow(new ServiceException("Test exception"));

        testGetAlbumsException(status().isServiceUnavailable(), ApplicationError.SERVICE_UNAVAILABLE);
    }

    @Test
    void testGetAlbumsUriBuildException() throws Exception {
        when(albumService.getAlbums(any()))
                .thenThrow(new UriBuildException(new Exception("Test exception")));

        testGetAlbumsException(status().isInternalServerError(), ApplicationError.SYS_ERR);
    }

    private void testGetAlbumsException(ResultMatcher status, ApplicationError error) throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/v1/artists/{amgArtistId}/albums", 15)
                .with(httpBasic("user", "pass"))
                .header(ApiHttpHeaders.USER_KEY_HEADER.getHeaderValue(), "testUUID")
                .param("limit", "5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        testException(status, error, mockHttpServletRequestBuilder);
    }

}
