package com.music.lover.hometask.service;


import com.music.lover.hometask.data.ArtistMock;
import com.music.lover.hometask.dto.ArtistResponse;
import com.music.lover.hometask.dto.NewArtistDTO;
import com.music.lover.hometask.entity.Artist;
import com.music.lover.hometask.entity.User;
import com.music.lover.hometask.exception.ArtistAlreadyExistsException;
import com.music.lover.hometask.exception.ServiceException;
import com.music.lover.hometask.exception.UriBuildException;
import com.music.lover.hometask.integration.response.ArtistInformation;
import com.music.lover.hometask.repository.ArtistRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArtistServiceImplTest {

    private ArtistServiceImpl artistService;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private ItunesService itunesService;

    @BeforeEach
    public void setup() {
        artistService = new ArtistServiceImpl(
                artistRepository,
                itunesService
        );
    }

    @Test
    void testGetArtist() throws Exception {
        ArtistInformation artistInformation = ArtistMock.getArtistInformation();

        when(itunesService.searchArtists(any()))
                .thenAnswer(invocation -> Collections.singletonList(artistInformation));

        List<ArtistResponse> artistResponseList = artistService.getArtist(artistInformation.getArtistName());

        verify(itunesService, times(1))
                .searchArtists(any());

        Assertions.assertFalse(artistResponseList.isEmpty());
        Assertions.assertEquals(artistInformation.getAmgArtistId(), artistResponseList.get(0).getAmgArtistId());
        Assertions.assertEquals(artistInformation.getArtistName(), artistResponseList.get(0).getArtistName());
    }

    @Test
    void testGetArtistServiceException() throws Exception {
        testGetArtistException(new ServiceException("TestException"), ServiceException.class);
    }

    @Test
    void testGetArtistUriBuildException() throws Exception {
        testGetArtistException(new UriBuildException(new Exception("TestException")), UriBuildException.class);
    }

    @Test
    void testSaveArtist() throws Exception {
        NewArtistDTO newArtistDTO = ArtistMock.getNewArtistDTO();

        when(artistRepository.existsByAmgArtistIdAndArtistUsersContaining(any(), any()))
                .thenAnswer(invocation -> false);

        when(artistRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArtistResponse artistResponse = artistService.saveArtist(newArtistDTO, new User());

        verify(artistRepository, times(1))
                .save(any());

        verify(artistRepository, times(1))
                .existsByAmgArtistIdAndArtistUsersContaining(any(), any());

        Assertions.assertNotNull(artistResponse);
        Assertions.assertEquals(newArtistDTO.getAmgArtistId(), artistResponse.getAmgArtistId());
        Assertions.assertEquals(newArtistDTO.getArtistName(), artistResponse.getArtistName());
    }

    @Test
    void testSaveArtistArtistAlreadyExistsException() {
        when(artistRepository.existsByAmgArtistIdAndArtistUsersContaining(any(), any()))
                .thenAnswer(invocation -> true);

        Assertions.assertThrows(ArtistAlreadyExistsException.class, () -> artistService.saveArtist(ArtistMock.getNewArtistDTO(), new User()));

        verify(artistRepository, times(1))
                .existsByAmgArtistIdAndArtistUsersContaining(any(), any());

        verify(artistRepository, times(0))
                .save(any());
    }

    @Test
    void testSaveOrGetArtist() {
        when(artistRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(artistRepository.findByAmgArtistId(any()))
                .thenAnswer(invocation -> Optional.empty());

        Artist artist = artistService.saveOrGetArtist(1L, "testArtist");

        verify(artistRepository, times(1))
                .save(any());

        verify(artistRepository, times(1))
                .findByAmgArtistId(any());

        Assertions.assertNotNull(artist);
        Assertions.assertEquals(1L, artist.getAmgArtistId());
        Assertions.assertEquals("testArtist", artist.getArtistName());
    }

    @Test
    void testSaveOrGetArtistFoundInDb() {
        Artist inputArtist = ArtistMock.getArtist();
        when(artistRepository.findByAmgArtistId(any()))
                .thenAnswer(invocation -> Optional.of(inputArtist));

        Artist artist = artistService.saveOrGetArtist(inputArtist.getAmgArtistId(), inputArtist.getArtistName());

        verify(artistRepository, times(0))
                .save(any());

        verify(artistRepository, times(1))
                .findByAmgArtistId(any());

        Assertions.assertNotNull(artist);
        Assertions.assertEquals(inputArtist.getAmgArtistId(), artist.getAmgArtistId());
        Assertions.assertEquals(inputArtist.getArtistName(), artist.getArtistName());
    }

    private <T extends Throwable> void testGetArtistException(Throwable exception, Class<T> exceptionClass) throws Exception {
        String artist = "testArtist";

        when(itunesService.searchArtists(any()))
                .thenThrow(exception);

        Assertions.assertThrows(exceptionClass, () -> artistService.getArtist(artist));

        verify(itunesService, times(1))
                .searchArtists(any());
    }

}
