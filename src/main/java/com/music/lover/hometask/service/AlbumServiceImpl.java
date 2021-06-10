package com.music.lover.hometask.service;

import com.music.lover.hometask.dto.AlbumResponse;
import com.music.lover.hometask.entity.Album;
import com.music.lover.hometask.entity.Artist;
import com.music.lover.hometask.exception.ServiceException;
import com.music.lover.hometask.exception.UriBuildException;
import com.music.lover.hometask.integration.response.AlbumInformation;
import com.music.lover.hometask.mapper.AlbumMapper;
import com.music.lover.hometask.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlbumServiceImpl implements AlbumService {

    private final ArtistService artistService;

    private final AlbumRepository albumRepository;

    private final ItunesService itunesService;

    private final int albumsRefreshDays;

    private final int albumsTopSize;

    public AlbumServiceImpl(
            ArtistService artistService,
            AlbumRepository albumRepository,
            ItunesService itunesService,
            @Value("${albums.refresh.days}") int albumsRefreshDays,
            @Value("${albums.top.size}") int albumsTopSize
    ) {
        this.artistService = artistService;
        this.albumRepository = albumRepository;
        this.itunesService = itunesService;
        this.albumsRefreshDays = albumsRefreshDays;
        this.albumsTopSize = albumsTopSize;
    }

    @Transactional
    @Cacheable(value = "albumsCache")
    public List<AlbumResponse> getAlbums(Long amgArtistId) throws ServiceException, UriBuildException {
        List<Album> savedAlbums = albumRepository.findAllByArtistAmgArtistId(amgArtistId);

        if (savedAlbums.isEmpty()) {
            savedAlbums = saveAlbums(amgArtistId, Optional.empty(), albumsTopSize);
        } else if (albumsTopSize > savedAlbums.size() || !checkIfAlbumValid(savedAlbums.get(0))) {
            Optional<Artist> artistOptional = Optional.of(savedAlbums.get(0).getArtist());

            albumRepository.deleteAllByArtistAmgArtistId(amgArtistId);

            savedAlbums = saveAlbums(amgArtistId, artistOptional, albumsTopSize);
        }

        return savedAlbums.stream()
                .limit(albumsTopSize)
                .map(AlbumMapper::toAlbumResponse)
                .collect(Collectors.toList());
    }

    private boolean checkIfAlbumValid(Album album) {
        LocalDate updateDateFrom = LocalDate.now().minusDays(albumsRefreshDays);

        return updateDateFrom.compareTo(album.getUpdateDate()) <= 0;
    }

    private List<Album> saveAlbums(
            Long amgArtistId,
            Optional<Artist> optionalArtist,
            int limit
    ) throws ServiceException, UriBuildException {
        List<AlbumInformation> fetchedAlbums = itunesService.lookupAlbums(amgArtistId, limit);

        if (fetchedAlbums.isEmpty()) {
            return new ArrayList<>();
        }

        Artist artist = optionalArtist.orElse(
                artistService.saveOrGetArtist(
                        fetchedAlbums.get(0).getAmgArtistId(),
                        fetchedAlbums.get(0).getArtistName()
                )
        );

        List<Album> albums = fetchedAlbums.stream()
                .map(albumInformation -> AlbumMapper.mapToAlbum(albumInformation, artist))
                .collect(Collectors.toList());

        return albumRepository.saveAll(albums);
    }


}
