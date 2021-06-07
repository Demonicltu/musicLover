package com.music.lover.hometask.dto;

public class ArtistDTO {

    private Long amgArtistId;

    private String artistName;

    public ArtistDTO(Long amgArtistId, String artistName) {
        this.amgArtistId = amgArtistId;
        this.artistName = artistName;
    }

    public Long getAmgArtistId() {
        return amgArtistId;
    }

    public void setAmgArtistId(Long amgArtistId) {
        this.amgArtistId = amgArtistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

}
