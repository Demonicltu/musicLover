package com.music.lover.hometask.service;

import com.music.lover.hometask.exception.ServiceException;
import com.music.lover.hometask.exception.UriBuildException;
import com.music.lover.hometask.integration.ItunesRestTemplate;
import com.music.lover.hometask.integration.response.AlbumInformation;
import com.music.lover.hometask.integration.response.AlbumInformationResponse;
import com.music.lover.hometask.integration.response.ArtistInformation;
import com.music.lover.hometask.integration.response.ArtistInformationResponse;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Service
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
            uriBuilder = new URIBuilder(SEARCH_API_PATH)
                    .addParameter("entity", "allArtist")
                    .addParameter("term", term);
        } catch (URISyntaxException e) {
            throw new UriBuildException(e);
        }

        ArtistInformationResponse artistInformationResponse = itunesRestTemplate.executePostRequest(
                uriBuilder.toString(),
                ArtistInformationResponse.class
        );

        return artistInformationResponse.getResults();
    }

    public List<AlbumInformation> lookupAlbums(Long amgArtistId, int limit) throws ServiceException, UriBuildException {
        URIBuilder uriBuilder;
        try {
            uriBuilder = new URIBuilder(LOOKUP_API_PATH)
                    .addParameter("entity", "album")
                    .addParameter("limit", String.valueOf(limit))
                    .addParameter("amgArtistId", String.valueOf(amgArtistId));
        } catch (URISyntaxException e) {
            throw new UriBuildException(e);
        }

        AlbumInformationResponse albumInformationResponse = itunesRestTemplate.executePostRequest(
                uriBuilder.toString(),
                AlbumInformationResponse.class
        );

        return albumInformationResponse.getResults().stream()
                .filter(albumInformation -> albumInformation.getWrapperType().equals("collection") &&
                        albumInformation.getCollectionType().equals("Album"))
                .collect(Collectors.toList());
    }

}
