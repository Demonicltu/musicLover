package com.music.lover.hometask.mapper;

import com.music.lover.hometask.dto.AlbumResponse;
import com.music.lover.hometask.entity.Album;
import com.music.lover.hometask.entity.Artist;
import com.music.lover.hometask.integration.response.AlbumInformation;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class AlbumMapper {

    public static Album mapToAlbum(AlbumInformation albumInformation, Artist artist) {
        return new Album(
                albumInformation.getCollectionName(),
                albumInformation.getCollectionPrice(),
                albumInformation.getCurrency(),
                albumInformation.getTrackCount(),
                albumInformation.getCopyright(),
                albumInformation.getCountry(),
                albumInformation.getPrimaryGenreName(),
                albumInformationReleaseDateToLocalDate(
                        albumInformation.getReleaseDate()
                ),
                artist
        );
    }

    public static AlbumResponse toAlbumResponse(Album album) {
        return new AlbumResponse(
                ArtistMapper.toArtistResponse(album.getArtist()),
                album.getCollectionName(),
                album.getCollectionPrice(),
                album.getCurrency(),
                album.getTrackCount(),
                album.getCopyright(),
                album.getCountry(),
                album.getPrimaryGenreName(),
                album.getReleaseDate()
        );
    }

    private static LocalDate albumInformationReleaseDateToLocalDate(String releaseDate) {
        Instant instant = Instant.parse(releaseDate);
        return LocalDate.ofInstant(instant, ZoneId.systemDefault());
    }

}
