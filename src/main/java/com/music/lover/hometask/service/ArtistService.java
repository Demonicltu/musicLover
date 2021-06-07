package com.music.lover.hometask.service;

import com.music.lover.hometask.dto.AlbumDTO;
import com.music.lover.hometask.dto.ArtistDTO;
import com.music.lover.hometask.dto.NewArtistDTO;
import com.music.lover.hometask.entity.User;
import com.music.lover.hometask.exception.ServiceException;
import com.music.lover.hometask.exception.UriBuildException;

import java.util.List;

public interface ArtistService {

    List<ArtistDTO> getArtists(String artist) throws ServiceException, UriBuildException;

    ArtistDTO saveFavouriteArtist(NewArtistDTO newArtistDTO, User user);

    List<AlbumDTO> getAlbums(List<Long> artistId, int limit) throws ServiceException, UriBuildException;

}
