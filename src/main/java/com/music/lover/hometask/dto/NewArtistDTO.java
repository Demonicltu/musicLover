package com.music.lover.hometask.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class NewArtistDTO {

    private final Long amgArtistId;

    private final String artistName;

    @JsonCreator
    public NewArtistDTO(
            @NotNull @Positive Long amgArtistId,
            @NotBlank String artistName
    ) {
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
