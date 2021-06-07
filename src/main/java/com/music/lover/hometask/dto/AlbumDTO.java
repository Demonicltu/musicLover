package com.music.lover.hometask.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AlbumDTO {

    private String artistName;

    private float collectionPrice;

    private String currency;

    private int trackCount;

    private String copyright;

    private String country;

    private String primaryGenreName;

    private LocalDate releaseDate;

    public AlbumDTO(String artistName, float collectionPrice, String currency, int trackCount, String copyright, String country, String primaryGenreName, LocalDate releaseDate) {
        this.artistName = artistName;
        this.collectionPrice = collectionPrice;
        this.currency = currency;
        this.trackCount = trackCount;
        this.copyright = copyright;
        this.country = country;
        this.primaryGenreName = primaryGenreName;
        this.releaseDate = releaseDate;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public float getCollectionPrice() {
        return collectionPrice;
    }

    public void setCollectionPrice(float collectionPrice) {
        this.collectionPrice = collectionPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPrimaryGenreName() {
        return primaryGenreName;
    }

    public void setPrimaryGenreName(String primaryGenreName) {
        this.primaryGenreName = primaryGenreName;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

}
