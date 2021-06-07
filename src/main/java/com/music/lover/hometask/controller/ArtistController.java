package com.music.lover.hometask.controller;


import com.music.lover.hometask.dto.AlbumDTO;
import com.music.lover.hometask.dto.ArtistDTO;
import com.music.lover.hometask.dto.AuthenticationInformation;
import com.music.lover.hometask.dto.NewArtistDTO;
import com.music.lover.hometask.exception.RequestException;
import com.music.lover.hometask.exception.ServiceException;
import com.music.lover.hometask.exception.UriBuildException;
import com.music.lover.hometask.exception.error.ApplicationError;
import com.music.lover.hometask.service.ArtistService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/v1/artists")
public class ArtistController implements BaseAuthenticatedController {

    private final ArtistService artistService;

    public ArtistController(
            ArtistService artistService
    ) {
        this.artistService = artistService;
    }

    @GetMapping("/search")
    public List<ArtistDTO> getArtists(
            @ModelAttribute("authenticationBeingUsed") AuthenticationInformation authenticationInformation,
            @RequestParam String artist
    ) {
        try {
            return artistService.getArtists(artist);
        } catch (ServiceException e) {
            throw new RequestException(ApplicationError.SERVICE_UNAVAILABLE);
        } catch (UriBuildException e) {
            throw new RequestException(ApplicationError.SYS_ERR);
        }
    }

    @PostMapping("/save")
    public ArtistDTO saveFavouriteArtist(
            @ModelAttribute("authenticationBeingUsed") AuthenticationInformation authenticationInformation,
            @RequestBody @Valid NewArtistDTO newArtistDTO
    ) {
        return artistService.saveFavouriteArtist(
                newArtistDTO,
                authenticationInformation.getUser()
        );
    }

    @GetMapping("/albums")
    public List<AlbumDTO> getAlbums(
            @ModelAttribute("authenticationBeingUsed") AuthenticationInformation authenticationInformation,
            @RequestParam(name = "amdArtistId") List<Long> amdArtistIdList,
            @RequestParam(required = false, defaultValue = "5") @Min(1) @Max(200) int limit
    ) {
        try {
            return artistService.getAlbums(amdArtistIdList, limit);
        } catch (ServiceException e) {
            throw new RequestException(ApplicationError.SERVICE_UNAVAILABLE);
        } catch (UriBuildException e) {
            throw new RequestException(ApplicationError.SYS_ERR);
        }
    }

}
