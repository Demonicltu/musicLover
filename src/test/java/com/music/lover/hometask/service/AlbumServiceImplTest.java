package com.music.lover.hometask.service;


import com.music.lover.hometask.data.AlbumMock;
import com.music.lover.hometask.dto.AlbumResponse;
import com.music.lover.hometask.entity.Album;
import com.music.lover.hometask.entity.Artist;
import com.music.lover.hometask.exception.ServiceException;
import com.music.lover.hometask.exception.UriBuildException;
import com.music.lover.hometask.integration.response.AlbumInformation;
import com.music.lover.hometask.repository.AlbumRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyInt;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AlbumServiceImplTest {

    private AlbumServiceImpl albumService;

    @Mock
    private ArtistService artistService;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ItunesService itunesService;

    @BeforeEach
    public void setup() {
        albumService = new AlbumServiceImpl(
                artistService,
                albumRepository,
                itunesService,
                21,
                5
        );
    }

    @Test
    void testGetAlbumsWithRefresh() throws Exception {
        Album album = AlbumMock.getAlbum();

        AlbumInformation albumInformation = AlbumMock.getAlbumInformation();

        when(albumRepository.findAllByArtistAmgArtistId(any()))
                .thenAnswer(invocation -> Collections.singletonList(album));

        when(itunesService.lookupAlbums(any(), anyInt()))
                .thenAnswer(invocation -> Collections.singletonList(albumInformation));

        when(albumRepository.saveAll(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<AlbumResponse> albumResponseList = albumService.getAlbums(1L);

        verify(albumRepository, times(1))
                .findAllByArtistAmgArtistId(any());

        verify(albumRepository, times(1))
                .deleteAllByArtistAmgArtistId(any());

        verify(itunesService, times(1))
                .lookupAlbums(any(), anyInt());

        verify(albumRepository, times(1))
                .saveAll(any());
        assertTestGetAlbums(albumResponseList, albumInformation);
    }

    @Test
    void testGetAlbumsNewFetch() throws Exception {
        AlbumInformation albumInformation = AlbumMock.getAlbumInformation();

        when(albumRepository.findAllByArtistAmgArtistId(any()))
                .thenAnswer(invocation -> new ArrayList<>());

        when(itunesService.lookupAlbums(any(), anyInt()))
                .thenAnswer(invocation -> Collections.singletonList(albumInformation));

        when(artistService.saveOrGetArtist(any(), any()))
                .thenAnswer(invocation -> {
                    Artist artist = new Artist();
                    artist.setAmgArtistId(invocation.getArgument(0));
                    artist.setArtistName(invocation.getArgument(1));

                    return artist;
                });

        when(albumRepository.saveAll(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<AlbumResponse> albumResponseList = albumService.getAlbums(1L);

        verify(albumRepository, times(1))
                .findAllByArtistAmgArtistId(any());

        verify(artistService, times(1))
                .saveOrGetArtist(any(), any());

        verify(itunesService, times(1))
                .lookupAlbums(any(), anyInt());

        verify(albumRepository, times(1))
                .saveAll(any());

        assertTestGetAlbums(albumResponseList, albumInformation);
    }

    @Test
    void testGetAlbumsServiceException() throws Exception {
        testGetAlbumsException(new ServiceException("Test exception"), ServiceException.class);
    }

    @Test
    void testGetAlbumsUriBuildException() throws Exception {
        testGetAlbumsException(new UriBuildException(new Exception("Test exception")), UriBuildException.class);
    }

    private <T extends Throwable> void testGetAlbumsException(Throwable exception, Class<T> exceptionClass) throws Exception {
        when(albumRepository.findAllByArtistAmgArtistId(any()))
                .thenAnswer(invocation -> new ArrayList<>());

        when(itunesService.lookupAlbums(any(), anyInt()))
                .thenThrow(exception);

        Assertions.assertThrows(exceptionClass, () -> albumService.getAlbums(1L));

        verify(albumRepository, times(1))
                .findAllByArtistAmgArtistId(any());

        verify(artistService, times(0))
                .saveArtist(any(), any());

        verify(itunesService, times(1))
                .lookupAlbums(any(), anyInt());

        verify(albumRepository, times(0))
                .saveAll(any());
    }

    private void assertTestGetAlbums(List<AlbumResponse> albumResponseList, AlbumInformation albumInformation) {
        Assertions.assertFalse(albumResponseList.isEmpty());
        Assertions.assertEquals(albumInformation.getArtistName(), albumResponseList.get(0).getArtistResponse().getArtistName());
        Assertions.assertEquals(albumInformation.getCollectionPrice(), albumResponseList.get(0).getCollectionPrice());
        Assertions.assertEquals(albumInformation.getCollectionName(), albumResponseList.get(0).getCollectionName());
        Assertions.assertEquals(albumInformation.getCurrency(), albumResponseList.get(0).getCurrency());
        Assertions.assertEquals(albumInformation.getTrackCount(), albumResponseList.get(0).getTrackCount());
        Assertions.assertEquals(albumInformation.getCopyright(), albumResponseList.get(0).getCopyright());
        Assertions.assertEquals(albumInformation.getCountry(), albumResponseList.get(0).getCountry());
        Assertions.assertEquals(albumInformation.getPrimaryGenreName(), albumResponseList.get(0).getPrimaryGenreName());
    }

}
