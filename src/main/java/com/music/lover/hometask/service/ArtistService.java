package com.music.lover.hometask.service;

import com.music.lover.hometask.dto.ArtistResponse;
import com.music.lover.hometask.dto.NewArtistDTO;
import com.music.lover.hometask.entity.Artist;
import com.music.lover.hometask.entity.User;
import com.music.lover.hometask.exception.ArtistAlreadyExistsException;
import com.music.lover.hometask.exception.ServiceException;
import com.music.lover.hometask.exception.UriBuildException;

import java.util.List;

public interface ArtistService {

    List<ArtistResponse> getArtist(String artist) throws ServiceException, UriBuildException;

    ArtistResponse saveArtist(NewArtistDTO newArtistDTO, User user) throws ArtistAlreadyExistsException;

    Artist saveOrGetArtist(Long amgArtistId, String artistName);

}
