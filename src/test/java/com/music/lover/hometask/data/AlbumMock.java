package com.music.lover.hometask.data;

import com.music.lover.hometask.dto.AlbumResponse;
import com.music.lover.hometask.dto.ArtistResponse;
import com.music.lover.hometask.entity.Album;
import com.music.lover.hometask.entity.Artist;
import com.music.lover.hometask.integration.response.AlbumInformation;
import com.music.lover.hometask.integration.response.AlbumInformationResponse;

import java.time.LocalDate;
import java.util.Collections;

public class AlbumMock {

    public static AlbumResponse getAlbumResponse() {
        return new AlbumResponse(
                new ArtistResponse(
                        1L,
                        "testArtistName"
                ),
                "testName",
                0.15f,
                "USD",
                15,
                "testCopyright",
                "testCountry",
                "testGenre",
                LocalDate.now()
        );
    }

    public static AlbumInformation getAlbumInformation() {
        AlbumInformation albumInformation = new AlbumInformation();
        albumInformation.setAmgArtistId(1L);
        albumInformation.setArtistName("testArtist");
        albumInformation.setCollectionName("TestName");
        albumInformation.setCollectionPrice(0.15f);
        albumInformation.setCurrency("USD");
        albumInformation.setTrackCount(15);
        albumInformation.setCopyright("testCopyright");
        albumInformation.setCountry("testCountry");
        albumInformation.setPrimaryGenreName("testGenre");
        albumInformation.setReleaseDate("2022-01-01T08:00:00Z");
        albumInformation.setWrapperType("collection");
        albumInformation.setCollectionType("Album");

        return albumInformation;
    }

    public static Album getAlbum() {
        Artist artist = new Artist();
        artist.setAmgArtistId(1L);
        artist.setArtistName("testArtist");

        Album album = new Album();
        album.setArtist(artist);
        album.setCollectionName("TestName");
        album.setCollectionPrice(0.15f);
        album.setCurrency("USD");
        album.setTrackCount(15);
        album.setCopyright("testCopyright");
        album.setCountry("testCountry");
        album.setPrimaryGenreName("testGenre");
        album.setReleaseDate(LocalDate.now());
        album.setUpdateDate(LocalDate.now().minusMonths(2));

        return album;
    }

    public static AlbumInformationResponse getAlbumInformationResponse() {
        AlbumInformationResponse albumInformationResponse = new AlbumInformationResponse();
        albumInformationResponse.setResultCount(1);
        albumInformationResponse.setResults(Collections.singletonList(getAlbumInformation()));

        return albumInformationResponse;
    }

}
