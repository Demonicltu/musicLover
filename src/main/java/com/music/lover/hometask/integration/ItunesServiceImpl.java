package com.music.lover.hometask.integration;

import com.music.lover.hometask.exception.ServiceException;
import com.music.lover.hometask.exception.UriBuildException;
import com.music.lover.hometask.integration.response.AlbumInformation;
import com.music.lover.hometask.integration.response.AlbumInformationResponse;
import com.music.lover.hometask.integration.response.ArtistInformation;
import com.music.lover.hometask.integration.response.ArtistInformationResponse;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItunesServiceImpl implements ItunesService {

    private final ItunesRestTemplate itunesRestTemplate;

    private static final String SEARCH_API_PATH = "search";
    private static final String LOOKUP_API_PATH = "lookup";

    public ItunesServiceImpl(ItunesRestTemplate itunesRestTemplate) {
        this.itunesRestTemplate = itunesRestTemplate;
    }

    public List<ArtistInformation> searchArtists(String term) throws ServiceException, UriBuildException {
        URIBuilder uriBuilder;
        try {
            uriBuilder = new URIBuilder(SEARCH_API_PATH);
        } catch (URISyntaxException e) {
            throw new UriBuildException(e);
        }
        uriBuilder.addParameter("entity", "allArtist");
        uriBuilder.addParameter("term", term);

        ArtistInformationResponse artistInformationResponse = itunesRestTemplate.executePostRequest(
                uriBuilder.toString(),
                ArtistInformationResponse.class
        );

        if (artistInformationResponse == null) {
            throw new ServiceException("Response is null");
        }

        return artistInformationResponse.getResults();
    }

    public List<AlbumInformation> lookupAlbums(List<Long> amgArtistIdList, int limit) throws ServiceException, UriBuildException {
        URIBuilder uriBuilder;
        try {
            uriBuilder = new URIBuilder(LOOKUP_API_PATH);
        } catch (URISyntaxException e) {
            throw new UriBuildException(e);
        }

        uriBuilder.addParameter("entity", "album");
        uriBuilder.addParameter("limit", String.valueOf(limit));

        for (Long amgArtistId : amgArtistIdList) {
            uriBuilder.addParameter("amgArtistId", String.valueOf(amgArtistId));
        }

        AlbumInformationResponse albumInformationResponse = itunesRestTemplate.executePostRequest(
                uriBuilder.toString(),
                AlbumInformationResponse.class
        );

        if (albumInformationResponse == null) {
            throw new ServiceException("Response is null");
        }

        return albumInformationResponse.getResults().stream()
                .filter(albumInformation -> albumInformation.getWrapperType().equals("collection") &&
                        albumInformation.getCollectionType().equals("Album"))
                .collect(Collectors.toList());
    }

}
