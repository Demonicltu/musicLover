package com.music.lover.hometask.dto;

public class ArtistResponse {

    private final Long amgArtistId;

    private final String artistName;

    public ArtistResponse(Long amgArtistId, String artistName) {
        this.amgArtistId = amgArtistId;
        this.artistName = artistName;
    }

    public Long getAmgArtistId() {
        return amgArtistId;
    }

    public String getArtistName() {
        return artistName;
    }


}
