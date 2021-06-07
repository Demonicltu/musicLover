package com.music.lover.hometask.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.music.lover.hometask.BaseTest;
import com.music.lover.hometask.constant.ApiHttpHeaders;
import com.music.lover.hometask.dto.AlbumDTO;
import com.music.lover.hometask.dto.ArtistDTO;
import com.music.lover.hometask.dto.NewArtistDTO;
import com.music.lover.hometask.entity.User;
import com.music.lover.hometask.exception.ServiceException;
import com.music.lover.hometask.exception.UriBuildException;
import com.music.lover.hometask.exception.UserNotFoundException;
import com.music.lover.hometask.exception.error.ApplicationError;
import com.music.lover.hometask.exception.error.RequestError;
import com.music.lover.hometask.service.ArtistService;
import com.music.lover.hometask.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ArtistControllerTest extends BaseTest {

    @MockBean
    private ArtistService artistService;

    @MockBean
    private UserService userService;

    @Test
    void testGetArtists() throws Exception {
        mockUserInterceptorActions();

        when(artistService.getArtists(any()))
                .thenAnswer(invocation -> {
                            ArtistDTO artistDTO = new ArtistDTO(
                                    1L,
                                    "TestArtist"
                            );

                            return Collections.singletonList(artistDTO);
                        }
                );

        MvcResult mvcResult = getMockMvc()
                .perform(
                        MockMvcRequestBuilders.get("/v1/artists/search")
                                .with(httpBasic("user", "pass"))
                                .header(ApiHttpHeaders.USER_KEY_HEADER.getHeaderValue(), "testUUID")
                                .param("artist", "testArtist")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(
                        document(
                                "getArtists",
                                preprocessRequest(removeHeaders("Content-Length", "Host"), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("Basic authentication header"),
                                        headerWithName(ApiHttpHeaders.USER_KEY_HEADER.getHeaderValue()).description("User unique id header")
                                ),
                                requestParameters(
                                        parameterWithName("artist").description("Searched artist")
                                ),
                                responseFields(
                                        fieldWithPath("[].amgArtistId").type(JsonFieldType.NUMBER).description("Artist amg id"),
                                        fieldWithPath("[].artistName").type(JsonFieldType.STRING).description("Artist name")
                                )
                        )
                )
                .andDo(print())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        List<ArtistDTO> artistDTOList = Arrays.asList(mapper.readValue(responseString, ArtistDTO[].class));

        Assertions.assertFalse(artistDTOList.isEmpty());
        Assertions.assertNotNull(artistDTOList.get(0).getAmgArtistId());
        Assertions.assertFalse(artistDTOList.get(0).getArtistName().isEmpty());
    }

    @Test
    void testGetArtistsServiceException() throws Exception {
        when(artistService.getArtists(any()))
                .thenThrow(new ServiceException("Test exception"));

        MvcResult mvcResult = getMockMvc()
                .perform(
                        MockMvcRequestBuilders.get("/v1/artists/search")
                                .with(httpBasic("user", "pass"))
                                .header(ApiHttpHeaders.USER_KEY_HEADER.getHeaderValue(), "testUUID")
                                .param("artist", "testArtist")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isServiceUnavailable())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();

        assertRequestError(responseString, ApplicationError.SERVICE_UNAVAILABLE);
    }

    @Test
    void testGetArtistsUriBuildException() throws Exception {
        when(artistService.getArtists(any()))
                .thenThrow(new UriBuildException(new Exception("Test exception")));

        MvcResult mvcResult = getMockMvc()
                .perform(
                        MockMvcRequestBuilders.get("/v1/artists/search")
                                .with(httpBasic("user", "pass"))
                                .header(ApiHttpHeaders.USER_KEY_HEADER.getHeaderValue(), "testUUID")
                                .param("artist", "testArtist")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isInternalServerError())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();

        assertRequestError(responseString, ApplicationError.SYS_ERR);
    }

    @Test
    void testSaveFavouriteArtist() throws Exception {
        mockUserInterceptorActions();

        NewArtistDTO newArtistDTO = new NewArtistDTO(
                1L,
                "testArtist"
        );

        when(artistService.saveFavouriteArtist(any(), any()))
                .thenAnswer(invocation -> new ArtistDTO(
                                1L,
                                "TestArtist"
                        )
                );

        MvcResult mvcResult = getMockMvc()
                .perform(
                        MockMvcRequestBuilders.post("/v1/artists/save")
                                .with(httpBasic("user", "pass"))
                                .header(ApiHttpHeaders.USER_KEY_HEADER.getHeaderValue(), "testUUID")
                                .content(mapper.writeValueAsString(newArtistDTO))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(
                        document(
                                "saveArtist",
                                preprocessRequest(removeHeaders("Content-Length", "Host"), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("Basic authentication header"),
                                        headerWithName(ApiHttpHeaders.USER_KEY_HEADER.getHeaderValue()).description("User unique id header")
                                ),
                                requestFields(
                                        fieldWithPath("amgArtistId").type(JsonFieldType.NUMBER).description("Artist amg id"),
                                        fieldWithPath("artistName").type(JsonFieldType.STRING).description("Artist name")
                                ),
                                responseFields(
                                        fieldWithPath("amgArtistId").type(JsonFieldType.NUMBER).description("Artist amg id"),
                                        fieldWithPath("artistName").type(JsonFieldType.STRING).description("Artist name")
                                )
                        )
                )
                .andDo(print())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        ArtistDTO artistDTO = mapper.readValue(responseString, ArtistDTO.class);

        Assertions.assertNotNull(artistDTO);
        Assertions.assertNotNull(artistDTO.getAmgArtistId());
        Assertions.assertFalse(artistDTO.getArtistName().isEmpty());
    }

    @Test
    void testGetAlbums() throws Exception {
        mockUserInterceptorActions();

        when(artistService.getAlbums(anyList(), anyInt()))
                .thenAnswer(invocation -> {
                            AlbumDTO albumDTO = new AlbumDTO(
                                    "testArtistName",
                                    0.15f,
                                    "USD",
                                    15,
                                    "testCopyright",
                                    "testountry",
                                    "testGenre",
                                    LocalDate.now()
                            );
                            return Collections.singletonList(albumDTO);
                        }
                );

        MvcResult mvcResult = getMockMvc()
                .perform(
                        MockMvcRequestBuilders.get("/v1/artists/albums")
                                .with(httpBasic("user", "pass"))
                                .header(ApiHttpHeaders.USER_KEY_HEADER.getHeaderValue(), "testUUID")
                                .param("amdArtistId", "15")
                                .param("amdArtistId", "16")
                                .param("limit", "5")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(
                        document(
                                "getAlbums",
                                preprocessRequest(removeHeaders("Content-Length", "Host"), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("Basic authentication header"),
                                        headerWithName(ApiHttpHeaders.USER_KEY_HEADER.getHeaderValue()).description("User unique id header")
                                ),
                                requestParameters(
                                        parameterWithName("amdArtistId").description("Artist amd id. Can be more than one parameter"),
                                        parameterWithName("limit").description("Response limit. Minimum value 1, maximum value 200, default value 5")
                                ),
                                responseFields(
                                        fieldWithPath("[].artistName").type(JsonFieldType.STRING).description("Artist name"),
                                        fieldWithPath("[].collectionPrice").type(JsonFieldType.NUMBER).description("Album price"),
                                        fieldWithPath("[].currency").type(JsonFieldType.STRING).description("Price currency"),
                                        fieldWithPath("[].trackCount").type(JsonFieldType.NUMBER).description("Number of tracks in album"),
                                        fieldWithPath("[].copyright").type(JsonFieldType.STRING).description("Copyright information"),
                                        fieldWithPath("[].country").type(JsonFieldType.STRING).description("Country"),
                                        fieldWithPath("[].primaryGenreName").type(JsonFieldType.STRING).description("Album genre"),
                                        fieldWithPath("[].releaseDate").type(JsonFieldType.STRING).description("Release date")
                                )
                        )
                )
                .andDo(print())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        List<AlbumDTO> albumDTOList = Arrays.asList(mapper.readValue(responseString, AlbumDTO[].class));

        Assertions.assertFalse(albumDTOList.isEmpty());
        Assertions.assertFalse(albumDTOList.get(0).getArtistName().isEmpty());
        Assertions.assertTrue(albumDTOList.get(0).getCollectionPrice() > 0);
        Assertions.assertFalse(albumDTOList.get(0).getCurrency().isEmpty());
        Assertions.assertTrue(albumDTOList.get(0).getTrackCount() > 0);
        Assertions.assertFalse(albumDTOList.get(0).getCopyright().isEmpty());
        Assertions.assertFalse(albumDTOList.get(0).getCountry().isEmpty());
        Assertions.assertFalse(albumDTOList.get(0).getPrimaryGenreName().isEmpty());
        Assertions.assertNotNull(albumDTOList.get(0).getReleaseDate());
    }

    @Test
    void testGetAlbumsServiceException() throws Exception {
        when(artistService.getAlbums(anyList(), anyInt()))
                .thenThrow(new ServiceException("Test exception"));

        MvcResult mvcResult = getMockMvc()
                .perform(
                        MockMvcRequestBuilders.get("/v1/artists/albums")
                                .with(httpBasic("user", "pass"))
                                .header(ApiHttpHeaders.USER_KEY_HEADER.getHeaderValue(), "testUUID")
                                .param("amdArtistId", "15")
                                .param("amdArtistId", "16")
                                .param("limit", "5")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isServiceUnavailable())
                .andReturn();


        String responseString = mvcResult.getResponse().getContentAsString();

        assertRequestError(responseString, ApplicationError.SERVICE_UNAVAILABLE);
    }

    @Test
    void testGetAlbumsUriBuildException() throws Exception {
        when(artistService.getAlbums(anyList(), anyInt()))
                .thenThrow(new UriBuildException(new Exception("Test exception")));

        MvcResult mvcResult = getMockMvc()
                .perform(
                        MockMvcRequestBuilders.get("/v1/artists/albums")
                                .with(httpBasic("user", "pass"))
                                .header(ApiHttpHeaders.USER_KEY_HEADER.getHeaderValue(), "testUUID")
                                .param("amdArtistId", "15")
                                .param("amdArtistId", "16")
                                .param("limit", "5")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isInternalServerError())
                .andReturn();


        String responseString = mvcResult.getResponse().getContentAsString();

        assertRequestError(responseString, ApplicationError.SYS_ERR);
    }

    private void mockUserInterceptorActions() throws UserNotFoundException {
        when(userService.getUserByUUID(any()))
                .thenAnswer(invocation -> new User());
    }

    private void assertRequestError(String response, ApplicationError applicationError) throws JsonProcessingException {
        RequestError error = mapper.readValue(response, RequestError.class);

        Assertions.assertEquals(applicationError.getErrorName(), error.getErrorName());
        Assertions.assertEquals(applicationError.getHttpStatus().toString(), error.getHttpStatus());
    }

}
