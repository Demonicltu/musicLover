package com.music.lover.hometask.controller;


import com.music.lover.hometask.BaseControllerTest;
import com.music.lover.hometask.constant.ApiHttpHeaders;
import com.music.lover.hometask.data.AlbumMock;
import com.music.lover.hometask.dto.AlbumResponse;
import com.music.lover.hometask.integration.ItunesRestTemplate;
import com.music.lover.hometask.integration.response.AlbumInformation;
import com.music.lover.hometask.integration.response.AlbumInformationResponse;
import com.music.lover.hometask.repository.AlbumRepository;
import com.music.lover.hometask.repository.ArtistRepository;
import com.music.lover.hometask.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AlbumControllerIntegrationTest extends BaseControllerTest {

    @MockBean
    private AlbumRepository albumRepository;

    @MockBean
    private ArtistRepository artistRepository;

    @MockBean
    private ItunesRestTemplate itunesRestTemplate;

    @MockBean
    private UserService userService;

    @Test
    void testGetAlbums() throws Exception {
        AlbumInformationResponse albumInformationResponse = AlbumMock.getAlbumInformationResponse();

        when(albumRepository.findAllByArtistAmgArtistId(any()))
                .thenAnswer(invocation -> new ArrayList<>());

        when(artistRepository.findByAmgArtistId(any()))
                .thenAnswer(invocation -> Optional.empty());

        when(itunesRestTemplate.executePostRequest(any(), any()))
                .thenAnswer(invocation -> albumInformationResponse);

        when(artistRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(albumRepository.saveAll(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MvcResult mvcResult = getMockMvc()
                .perform(
                        RestDocumentationRequestBuilders.get("/v1/artists/{amgArtistId}/albums", 15)
                                .with(httpBasic("user", "pass"))
                                .header(ApiHttpHeaders.USER_KEY_HEADER.getHeaderValue(), "testUUID")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(documentGetAlbums())
                .andDo(print())
                .andReturn();

        verify(albumRepository, times(1))
                .findAllByArtistAmgArtistId(any());

        verify(artistRepository, times(1))
                .findByAmgArtistId(any());

        verify(itunesRestTemplate, times(1))
                .executePostRequest(any(), any());

        verify(artistRepository, times(1))
                .save(any());

        verify(albumRepository, times(1))
                .saveAll(any());

        String responseString = mvcResult.getResponse().getContentAsString();
        List<AlbumResponse> albumResponseList = Arrays.asList(mapper.readValue(responseString, AlbumResponse[].class));

        AlbumInformation mockedAlbumInformation = albumInformationResponse.getResults().get(0);

        AlbumResponse response = albumResponseList.get(0);

        Assertions.assertEquals(mockedAlbumInformation.getArtistName(), response.getArtistResponse().getArtistName());
        Assertions.assertEquals(mockedAlbumInformation.getAmgArtistId(), response.getArtistResponse().getAmgArtistId());
        Assertions.assertEquals(mockedAlbumInformation.getCollectionPrice(), response.getCollectionPrice());
        Assertions.assertEquals(mockedAlbumInformation.getCollectionName(), response.getCollectionName());
        Assertions.assertEquals(mockedAlbumInformation.getCurrency(), response.getCurrency());
        Assertions.assertEquals(mockedAlbumInformation.getTrackCount(), response.getTrackCount());
        Assertions.assertEquals(mockedAlbumInformation.getCopyright(), response.getCopyright());
        Assertions.assertEquals(mockedAlbumInformation.getCountry(), response.getCountry());
        Assertions.assertEquals(mockedAlbumInformation.getPrimaryGenreName(), response.getPrimaryGenreName());
        Assertions.assertNotNull(response.getReleaseDate());
    }

    private RestDocumentationResultHandler documentGetAlbums() {
        return document(
                "getAlbums",
                preprocessRequest(removeHeaders("Content-Length", "Host"), prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName("Authorization").description("Basic authentication header"),
                        headerWithName(ApiHttpHeaders.USER_KEY_HEADER.getHeaderValue()).description("User unique id header")
                ),
                pathParameters(
                        parameterWithName("amgArtistId").description("Artist amg id")
                ),
                responseFields(
                        fieldWithPath("[].artistResponse.artistName").type(JsonFieldType.STRING).description("Artist name"),
                        fieldWithPath("[].artistResponse.amgArtistId").type(JsonFieldType.NUMBER).description("Artist amg id"),
                        fieldWithPath("[].collectionName").type(JsonFieldType.STRING).description("Album name"),
                        fieldWithPath("[].collectionPrice").type(JsonFieldType.NUMBER).description("Album price"),
                        fieldWithPath("[].currency").type(JsonFieldType.STRING).description("Price currency"),
                        fieldWithPath("[].trackCount").type(JsonFieldType.NUMBER).description("Number of tracks in album"),
                        fieldWithPath("[].copyright").type(JsonFieldType.STRING).description("Copyright information"),
                        fieldWithPath("[].country").type(JsonFieldType.STRING).description("Country"),
                        fieldWithPath("[].primaryGenreName").type(JsonFieldType.STRING).description("Album genre"),
                        fieldWithPath("[].releaseDate").type(JsonFieldType.STRING).description("Release date")
                )
        );
    }

}
