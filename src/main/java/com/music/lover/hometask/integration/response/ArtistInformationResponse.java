package com.music.lover.hometask.integration.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ArtistInformationResponse {

    private int resultCount;

    private List<ArtistInformation> results;

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public List<ArtistInformation> getResults() {
        return results;
    }

    public void setResults(List<ArtistInformation> results) {
        this.results = results;
    }

}
