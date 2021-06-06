package com.music.lover.hometask.service;

import com.music.lover.hometask.repository.FavouriteArtistRepository;
import org.springframework.stereotype.Service;

@Service
public class ArtistServiceImpl implements ArtistService {

    private final FavouriteArtistRepository favouriteArtistRepository;

    public ArtistServiceImpl(FavouriteArtistRepository favouriteArtistRepository) {
        this.favouriteArtistRepository = favouriteArtistRepository;
    }



}
