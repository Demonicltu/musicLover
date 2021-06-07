package com.music.lover.hometask.integration;

import com.music.lover.hometask.exception.ServiceException;
import com.music.lover.hometask.exception.UriBuildException;
import com.music.lover.hometask.integration.response.AlbumInformation;
import com.music.lover.hometask.integration.response.ArtistInformation;

import java.util.List;

public interface ItunesService {

    List<ArtistInformation> searchArtists(String term) throws ServiceException, UriBuildException;

    List<AlbumInformation> lookupAlbums(List<Long> amgArtistIdList, int limit) throws ServiceException, UriBuildException;

}
