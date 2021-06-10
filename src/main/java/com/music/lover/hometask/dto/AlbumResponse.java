package com.music.lover.hometask.dto;

import java.time.LocalDate;

public class AlbumResponse {

    private final ArtistResponse artistResponse;

    private final String collectionName;

    private final float collectionPrice;

    private final String currency;

    private final int trackCount;

    private final String copyright;

    private final String country;

    private final String primaryGenreName;

    private final LocalDate releaseDate;

    public AlbumResponse(
            ArtistResponse artistResponse,
            String collectionName,
            float collectionPrice,
            String currency,
            int trackCount,
            String copyright,
            String country,
            String primaryGenreName,
            LocalDate releaseDate
    ) {
        this.artistResponse = artistResponse;
        this.collectionName = collectionName;
        this.collectionPrice = collectionPrice;
        this.currency = currency;
        this.trackCount = trackCount;
        this.copyright = copyright;
        this.country = country;
        this.primaryGenreName = primaryGenreName;
        this.releaseDate = releaseDate;
    }

    public ArtistResponse getArtistResponse() {
        return artistResponse;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public float getCollectionPrice() {
        return collectionPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getCountry() {
        return country;
    }

    public String getPrimaryGenreName() {
        return primaryGenreName;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

}
