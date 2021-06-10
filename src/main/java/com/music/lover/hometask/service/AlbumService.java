package com.music.lover.hometask.service;

import com.music.lover.hometask.dto.AlbumResponse;
import com.music.lover.hometask.exception.ServiceException;
import com.music.lover.hometask.exception.UriBuildException;

import java.util.List;

public interface AlbumService {
    
    List<AlbumResponse> getAlbums(Long amgArtistId) throws ServiceException, UriBuildException;
    
}
