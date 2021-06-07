package com.music.lover.hometask.service;


import com.music.lover.hometask.dto.AlbumDTO;
import com.music.lover.hometask.dto.ArtistDTO;
import com.music.lover.hometask.dto.NewArtistDTO;
import com.music.lover.hometask.entity.User;
import com.music.lover.hometask.exception.ServiceException;
import com.music.lover.hometask.exception.UriBuildException;
import com.music.lover.hometask.integration.ItunesService;
import com.music.lover.hometask.integration.response.AlbumInformation;
import com.music.lover.hometask.integration.response.ArtistInformation;
import com.music.lover.hometask.repository.FavouriteArtistRepository;
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
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyInt;

@ExtendWith(MockitoExtension.class)
class ArtistServiceImplTest {

    @InjectMocks
    private ArtistServiceImpl artistService;

    @Mock
    private FavouriteArtistRepository favouriteArtistRepository;

    @Mock
    private ItunesService itunesService;

    @Test
    void testGetArtists() throws Exception {
        String artist = "testArtist";

        ArtistInformation artistInformation = new ArtistInformation();
        artistInformation.setArtistName("testArtist");
        artistInformation.setAmgArtistId(1L);

        when(itunesService.searchArtists(any()))
                .thenAnswer(invocation -> Collections.singletonList(artistInformation));

        List<ArtistDTO> artistDTOList = artistService.getArtists(artist);

        verify(itunesService, times(1))
                .searchArtists(any());

        Assertions.assertFalse(artistDTOList.isEmpty());
        Assertions.assertEquals(artistInformation.getAmgArtistId(), artistDTOList.get(0).getAmgArtistId());
        Assertions.assertEquals(artistInformation.getArtistName(), artistDTOList.get(0).getArtistName());
    }

    @Test
    void testGetArtistsServiceException() throws Exception {
        String artist = "testArtist";

        when(itunesService.searchArtists(any()))
                .thenThrow(new ServiceException("TestException"));

        Assertions.assertThrows(ServiceException.class, () -> artistService.getArtists(artist));

        verify(itunesService, times(1))
                .searchArtists(any());
    }

    @Test
    void testGetArtistsUriBuildException() throws Exception {
        String artist = "testArtist";

        when(itunesService.searchArtists(any()))
                .thenThrow(new UriBuildException(new Exception("TestException")));

        Assertions.assertThrows(UriBuildException.class, () -> artistService.getArtists(artist));

        verify(itunesService, times(1))
                .searchArtists(any());
    }

    @Test
    void testSaveFavouriteArtist() {
        NewArtistDTO newArtistDTO = new NewArtistDTO(
                1L,
                "testArtist"
        );

        when(favouriteArtistRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArtistDTO artistDTO = artistService.saveFavouriteArtist(newArtistDTO, new User());

        verify(favouriteArtistRepository, times(1))
                .save(any());

        Assertions.assertNotNull(artistDTO);
        Assertions.assertEquals(newArtistDTO.getAmgArtistId(), artistDTO.getAmgArtistId());
        Assertions.assertEquals(newArtistDTO.getArtistName(), artistDTO.getArtistName());
    }

    @Test
    void testGetAlbums() throws Exception {
        AlbumInformation albumInformation = new AlbumInformation();
        albumInformation.setAmgArtistId(1L);
        albumInformation.setArtistName("testArtist");
        albumInformation.setCollectionPrice(0.15f);
        albumInformation.setCurrency("USD");
        albumInformation.setTrackCount(15);
        albumInformation.setCopyright("testCopyright");
        albumInformation.setCountry("testCountry");
        albumInformation.setPrimaryGenreName("testGenre");
        albumInformation.setReleaseDate("2012-01-01T08:00:00Z");

        when(itunesService.lookupAlbums(anyList(), anyInt()))
                .thenAnswer(invocation -> Collections.singletonList(albumInformation));

        List<AlbumDTO> albumDTOList = artistService.getAlbums(Collections.singletonList(1L), 5);

        verify(itunesService, times(1))
                .lookupAlbums(anyList(), anyInt());

        Assertions.assertFalse(albumDTOList.isEmpty());
        Assertions.assertEquals(albumInformation.getArtistName(), albumDTOList.get(0).getArtistName());
        Assertions.assertEquals(albumInformation.getCollectionPrice(), albumDTOList.get(0).getCollectionPrice());
        Assertions.assertEquals(albumInformation.getCurrency(), albumDTOList.get(0).getCurrency());
        Assertions.assertEquals(albumInformation.getTrackCount(), albumDTOList.get(0).getTrackCount());
        Assertions.assertEquals(albumInformation.getCopyright(), albumDTOList.get(0).getCopyright());
        Assertions.assertEquals(albumInformation.getCountry(), albumDTOList.get(0).getCountry());
        Assertions.assertEquals(albumInformation.getPrimaryGenreName(), albumDTOList.get(0).getPrimaryGenreName());
    }

    @Test
    void testGetAlbumsServiceException() throws Exception {
        when(itunesService.lookupAlbums(anyList(), anyInt()))
                .thenThrow(new ServiceException("TestException"));

        Assertions.assertThrows(ServiceException.class, () -> artistService.getAlbums(Collections.singletonList(1L), 5));

        verify(itunesService, times(1))
                .lookupAlbums(anyList(), anyInt());
    }

    @Test
    void testGetAlbumsUriBuildException() throws Exception {
        when(itunesService.lookupAlbums(anyList(), anyInt()))
                .thenThrow(new UriBuildException(new Exception("TestException")));

        Assertions.assertThrows(UriBuildException.class, () -> artistService.getAlbums(Collections.singletonList(1L), 5));

        verify(itunesService, times(1))
                .lookupAlbums(anyList(), anyInt());
    }

}
