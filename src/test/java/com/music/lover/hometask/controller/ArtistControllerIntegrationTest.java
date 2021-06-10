package com.music.lover.hometask.controller;

import com.music.lover.hometask.BaseControllerTest;
import com.music.lover.hometask.constant.ApiHttpHeaders;
import com.music.lover.hometask.data.ArtistMock;
import com.music.lover.hometask.dto.ArtistResponse;
import com.music.lover.hometask.dto.NewArtistDTO;
import com.music.lover.hometask.entity.User;
import com.music.lover.hometask.integration.ItunesRestTemplate;
import com.music.lover.hometask.integration.response.ArtistInformationResponse;
import com.music.lover.hometask.repository.ArtistRepository;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ArtistControllerIntegrationTest extends BaseControllerTest {

    @MockBean
    private ArtistRepository artistRepository;

    @MockBean
    private ItunesRestTemplate itunesRestTemplate;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testGetArtist() throws Exception {
        ArtistInformationResponse artistInformationResponse = ArtistMock.getArtistInformationResponse();
        when(itunesRestTemplate.executePostRequest(any(), any()))
                .thenAnswer(invocation -> artistInformationResponse);

        when(userRepository.findByUuid(any()))
                .thenAnswer(invocation -> Optional.of(new User()));

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
                .andDo(documentGetArtist())
                .andDo(print())
                .andReturn();

        verify(userRepository, times(1))
                .findByUuid(any());

        verify(itunesRestTemplate, times(1))
                .executePostRequest(any(), any());

        String responseString = mvcResult.getResponse().getContentAsString();
        List<ArtistResponse> artistResponseList = Arrays.asList(mapper.readValue(responseString, ArtistResponse[].class));

        Assertions.assertFalse(artistResponseList.isEmpty());
        Assertions.assertEquals(artistInformationResponse.getResults().get(0).getAmgArtistId(), artistResponseList.get(0).getAmgArtistId());
        Assertions.assertEquals(artistInformationResponse.getResults().get(0).getArtistName(), artistResponseList.get(0).getArtistName());
    }

    @Test
    void testSaveArtist() throws Exception {
        NewArtistDTO artistDTO = ArtistMock.getNewArtistDTO();
        when(artistRepository.existsByAmgArtistIdAndArtistUsersContaining(any(), any()))
                .thenAnswer(invocation -> false);

        when(artistRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(userRepository.findByUuid(any()))
                .thenAnswer(invocation -> Optional.of(new User()));

        MvcResult mvcResult = getMockMvc()
                .perform(
                        MockMvcRequestBuilders.post("/v1/artists")
                                .with(httpBasic("user", "pass"))
                                .header(ApiHttpHeaders.USER_KEY_HEADER.getHeaderValue(), "testUUID")
                                .content(mapper.writeValueAsString(artistDTO))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andDo(documentSaveArtist())
                .andDo(print())
                .andReturn();

        verify(userRepository, times(1))
                .findByUuid(any());

        verify(artistRepository, times(1))
                .existsByAmgArtistIdAndArtistUsersContaining(any(), any());

        verify(artistRepository, times(1))
                .save(any());

        String responseString = mvcResult.getResponse().getContentAsString();
        ArtistResponse artistResponse = mapper.readValue(responseString, ArtistResponse.class);

        Assertions.assertNotNull(artistResponse);
        Assertions.assertEquals(artistDTO.getAmgArtistId(), artistResponse.getAmgArtistId());
        Assertions.assertEquals(artistDTO.getArtistName(), artistResponse.getArtistName());
    }

    private RestDocumentationResultHandler documentGetArtist() {
        return document(
                "getArtist",
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
        );
    }

    private RestDocumentationResultHandler documentSaveArtist() {
        return document(
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
        );
    }

}
