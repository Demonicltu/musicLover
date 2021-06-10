package com.music.lover.hometask.controller;


import com.music.lover.hometask.dto.ArtistResponse;
import com.music.lover.hometask.dto.AuthenticationInformation;
import com.music.lover.hometask.dto.NewArtistDTO;
import com.music.lover.hometask.exception.RequestException;
import com.music.lover.hometask.exception.ServiceException;
import com.music.lover.hometask.exception.UriBuildException;
import com.music.lover.hometask.exception.ArtistAlreadyExistsException;
import com.music.lover.hometask.exception.error.ApplicationError;
import com.music.lover.hometask.service.ArtistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/artists", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ArtistController implements BaseAuthenticatedController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public List<ArtistResponse> getArtist(
            @ModelAttribute("authenticationBeingUsed") AuthenticationInformation authenticationInformation,
            @RequestParam String artist
    ) {
        try {
            return artistService.getArtist(artist);
        } catch (ServiceException e) {
            throw new RequestException(ApplicationError.SERVICE_UNAVAILABLE);
        } catch (UriBuildException e) {
            throw new RequestException(ApplicationError.SYS_ERR);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArtistResponse saveArtist(
            @ModelAttribute("authenticationBeingUsed") AuthenticationInformation authenticationInformation,
            @RequestBody @Valid NewArtistDTO newArtistDTO
    ) {
        try {
            return artistService.saveArtist(
                    newArtistDTO,
                    authenticationInformation.getUser()
            );
        } catch (ArtistAlreadyExistsException e) {
            throw new RequestException(ApplicationError.ARTIST_ALREADY_SAVED);
        }
    }

}
