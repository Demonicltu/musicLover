package com.music.lover.hometask.mapper;

import com.music.lover.hometask.dto.ArtistResponse;
import com.music.lover.hometask.entity.Artist;

public class ArtistMapper {

    public static ArtistResponse toArtistResponse(Artist artist) {
        return new ArtistResponse(
                artist.getAmgArtistId(),
                artist.getArtistName()
        );
    }

}
