package com.music.lover.hometask.integration.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbumInformationResponse {

    private int resultCount;

    private List<AlbumInformation> results;

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public List<AlbumInformation> getResults() {
        return results;
    }

    public void setResults(List<AlbumInformation> results) {
        this.results = results;
    }

}
