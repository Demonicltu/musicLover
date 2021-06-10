package com.music.lover.hometask.service;

import com.music.lover.hometask.data.AlbumMock;
import com.music.lover.hometask.data.ArtistMock;
import com.music.lover.hometask.integration.ItunesRestTemplate;
import com.music.lover.hometask.integration.response.AlbumInformation;
import com.music.lover.hometask.integration.response.AlbumInformationResponse;
import com.music.lover.hometask.integration.response.ArtistInformation;
import com.music.lover.hometask.integration.response.ArtistInformationResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ItunesServiceImplTest {

    @InjectMocks
    private ItunesServiceImpl itunesService;

    @Mock
    private ItunesRestTemplate itunesRestTemplate;

    @Test
    void testSearchArtists() throws Exception {
        ArtistInformation artistInformation = ArtistMock.getArtistInformation();

        when(itunesRestTemplate.executePostRequest(any(), any()))
                .thenAnswer(invocation -> {
                    ArtistInformationResponse response = new ArtistInformationResponse();

                    response.setResults(Collections.singletonList(artistInformation));
                    return response;
                });

        List<ArtistInformation> artistInformationList = itunesService.searchArtists(artistInformation.getArtistName());

        verify(itunesRestTemplate, times(1))
                .executePostRequest(any(), any());

        Assertions.assertFalse(artistInformationList.isEmpty());
        Assertions.assertEquals(artistInformation.getAmgArtistId(), artistInformationList.get(0).getAmgArtistId());
        Assertions.assertEquals(artistInformation.getArtistName(), artistInformationList.get(0).getArtistName());
    }

    @Test
    void testLookupAlbums() throws Exception {
        AlbumInformation albumInformation = AlbumMock.getAlbumInformation();

        when(itunesRestTemplate.executePostRequest(any(), any()))
                .thenAnswer(invocation -> {
                    AlbumInformationResponse response = new AlbumInformationResponse();

                    response.setResults(Collections.singletonList(albumInformation));
                    return response;
                });

        List<AlbumInformation> albumInformationList = itunesService.lookupAlbums(1L, 1);

        verify(itunesRestTemplate, times(1))
                .executePostRequest(any(), any());

        Assertions.assertFalse(albumInformationList.isEmpty());
        Assertions.assertEquals(albumInformation.getAmgArtistId(), albumInformationList.get(0).getAmgArtistId());
        Assertions.assertEquals(albumInformation.getArtistName(), albumInformationList.get(0).getArtistName());
        Assertions.assertEquals(albumInformation.getCollectionName(), albumInformationList.get(0).getCollectionName());
        Assertions.assertEquals(albumInformation.getCollectionPrice(), albumInformationList.get(0).getCollectionPrice());
        Assertions.assertEquals(albumInformation.getCurrency(), albumInformationList.get(0).getCurrency());
        Assertions.assertEquals(albumInformation.getTrackCount(), albumInformationList.get(0).getTrackCount());
        Assertions.assertEquals(albumInformation.getCopyright(), albumInformationList.get(0).getCopyright());
        Assertions.assertEquals(albumInformation.getCountry(), albumInformationList.get(0).getCountry());
        Assertions.assertEquals(albumInformation.getPrimaryGenreName(), albumInformationList.get(0).getPrimaryGenreName());
        Assertions.assertEquals(albumInformation.getReleaseDate(), albumInformationList.get(0).getReleaseDate());
        Assertions.assertEquals(albumInformation.getCollectionType(), albumInformationList.get(0).getCollectionType());
        Assertions.assertEquals(albumInformation.getWrapperType(), albumInformationList.get(0).getWrapperType());
    }

}
