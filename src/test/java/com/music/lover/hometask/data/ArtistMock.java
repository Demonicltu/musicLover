package com.music.lover.hometask.data;

import com.music.lover.hometask.dto.ArtistResponse;
import com.music.lover.hometask.dto.NewArtistDTO;
import com.music.lover.hometask.entity.Artist;
import com.music.lover.hometask.integration.response.ArtistInformation;
import com.music.lover.hometask.integration.response.ArtistInformationResponse;

import java.util.Collections;


public class ArtistMock {

    public static ArtistResponse getArtistResponse() {
        return new ArtistResponse(
                1L,
                "TestArtist"
        );
    }

    public static ArtistInformation getArtistInformation() {
        ArtistInformation artistInformation = new ArtistInformation();
        artistInformation.setArtistName("testArtist");
        artistInformation.setAmgArtistId(1L);

        return artistInformation;
    }

    public static Artist getArtist() {
        Artist artist = new Artist();
        artist.setAmgArtistId(1L);
        artist.setArtistName("Test artist");

        return artist;
    }

    public static NewArtistDTO getNewArtistDTO() {
        return new NewArtistDTO(
                1L,
                "testArtist"
        );
    }

    public static ArtistInformationResponse getArtistInformationResponse() {
        ArtistInformationResponse artistInformationResponse = new ArtistInformationResponse();
        artistInformationResponse.setResultCount(1);
        artistInformationResponse.setResults(Collections.singletonList(getArtistInformation()));

        return artistInformationResponse;
    }

}
