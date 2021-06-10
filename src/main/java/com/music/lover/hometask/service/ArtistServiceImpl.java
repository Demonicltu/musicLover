package com.music.lover.hometask.service;

import com.music.lover.hometask.dto.ArtistResponse;
import com.music.lover.hometask.dto.NewArtistDTO;
import com.music.lover.hometask.entity.Artist;
import com.music.lover.hometask.entity.User;
import com.music.lover.hometask.exception.ArtistAlreadyExistsException;
import com.music.lover.hometask.exception.ServiceException;
import com.music.lover.hometask.exception.UriBuildException;
import com.music.lover.hometask.integration.response.ArtistInformation;
import com.music.lover.hometask.mapper.ArtistMapper;
import com.music.lover.hometask.repository.ArtistRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;

    private final ItunesService itunesService;

    public ArtistServiceImpl(
            ArtistRepository artistRepository,
            ItunesService itunesService
    ) {
        this.artistRepository = artistRepository;
        this.itunesService = itunesService;
    }

    @Cacheable(value = "artistsCache")
    public List<ArtistResponse> getArtist(String artist) throws ServiceException, UriBuildException {
        List<ArtistInformation> artistInformationList = itunesService.searchArtists(artist);

        return artistInformationList.stream()
                .map(artistInformation -> new ArtistResponse(
                                artistInformation.getAmgArtistId(),
                                artistInformation.getArtistName()
                        )
                )
                .collect(Collectors.toList());
    }

    public ArtistResponse saveArtist(NewArtistDTO newArtistDTO, User user) throws ArtistAlreadyExistsException {
        if (artistRepository.existsByAmgArtistIdAndArtistUsersContaining(newArtistDTO.getAmgArtistId(), user)) {
            throw new ArtistAlreadyExistsException();
        }

        Artist artist = new Artist(
                newArtistDTO.getAmgArtistId(),
                newArtistDTO.getArtistName(),
                user
        );

        return ArtistMapper.toArtistResponse(artistRepository.save(artist));
    }

    public Artist saveOrGetArtist(Long amgArtistId, String artistName) {
        Optional<Artist> optionalArtist = artistRepository.findByAmgArtistId(amgArtistId);
        if (optionalArtist.isPresent()) {
            return optionalArtist.get();
        }

        Artist newArtist = new Artist();
        newArtist.setAmgArtistId(amgArtistId);
        newArtist.setArtistName(artistName);

        return artistRepository.save(newArtist);
    }

}
