package com.music.lover.hometask.service;

import com.music.lover.hometask.dto.AlbumDTO;
import com.music.lover.hometask.dto.ArtistDTO;
import com.music.lover.hometask.dto.NewArtistDTO;
import com.music.lover.hometask.entity.FavouriteArtist;
import com.music.lover.hometask.entity.User;
import com.music.lover.hometask.exception.ServiceException;
import com.music.lover.hometask.exception.UriBuildException;
import com.music.lover.hometask.integration.ItunesService;
import com.music.lover.hometask.integration.response.AlbumInformation;
import com.music.lover.hometask.integration.response.ArtistInformation;
import com.music.lover.hometask.repository.FavouriteArtistRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtistServiceImpl implements ArtistService {

    private final FavouriteArtistRepository favouriteArtistRepository;

    private final ItunesService itunesService;

    public ArtistServiceImpl(
            FavouriteArtistRepository favouriteArtistRepository,
            ItunesService itunesService
    ) {
        this.favouriteArtistRepository = favouriteArtistRepository;
        this.itunesService = itunesService;
    }

    @Cacheable(value = "artistsCache")
    public List<ArtistDTO> getArtists(String artist) throws ServiceException, UriBuildException {
        List<ArtistInformation> artistInformationList = itunesService.searchArtists(artist);

        return artistInformationList.stream()
                .map(this::toArtistDTO)
                .collect(Collectors.toList());
    }

    public ArtistDTO saveFavouriteArtist(NewArtistDTO newArtistDTO, User user) {
        FavouriteArtist favouriteArtist = new FavouriteArtist(
                newArtistDTO.getAmgArtistId(),
                newArtistDTO.getArtistName(),
                user
        );

        FavouriteArtist savedFavouriteArtist = favouriteArtistRepository.save(favouriteArtist);

        return toArtistDTO(savedFavouriteArtist);
    }

    @Cacheable(value = "artistAlbumsCache")
    public List<AlbumDTO> getAlbums(List<Long> artistIds, int limit) throws ServiceException, UriBuildException {
        List<AlbumInformation> albumInformationList = itunesService.lookupAlbums(artistIds, limit);

        return albumInformationList.stream()
                .map(this::toAlbumDTO)
                .collect(Collectors.toList());
    }

    private ArtistDTO toArtistDTO(ArtistInformation artistInformation) {
        return new ArtistDTO(
                artistInformation.getAmgArtistId(),
                artistInformation.getArtistName()
        );
    }

    private ArtistDTO toArtistDTO(FavouriteArtist favouriteArtist) {
        return new ArtistDTO(
                favouriteArtist.getAmgArtistId(),
                favouriteArtist.getArtistName()
        );
    }

    private AlbumDTO toAlbumDTO(AlbumInformation albumInformation) {
        Instant instant = Instant.parse(albumInformation.getReleaseDate());
        LocalDate localDate = LocalDate.ofInstant(instant, ZoneId.systemDefault());

        return new AlbumDTO(
                albumInformation.getArtistName(),
                albumInformation.getCollectionPrice(),
                albumInformation.getCurrency(),
                albumInformation.getTrackCount(),
                albumInformation.getCopyright(),
                albumInformation.getCountry(),
                albumInformation.getPrimaryGenreName(),
                localDate
        );
    }

}
