package com.music.lover.hometask.service;


import com.music.lover.hometask.repository.FavouriteArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(ArtistService.class)
public class ArtistServiceTest {

    private final ArtistService artistService;

    @MockBean
    private FavouriteArtistRepository favouriteArtistRepository;

    @Autowired
    public ArtistServiceTest(ArtistService artistService) {
        this.artistService = artistService;
    }

}
