package com.music.lover.hometask.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class NewArtistDTO {

    @NotNull
    private Long amgArtistId;

    @NotBlank
    private String artistName;

    public NewArtistDTO(Long amgArtistId, String artistName) {
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
