package com.music.lover.hometask.controller;


import com.music.lover.hometask.dto.AlbumResponse;
import com.music.lover.hometask.dto.AuthenticationInformation;
import com.music.lover.hometask.exception.RequestException;
import com.music.lover.hometask.exception.ServiceException;
import com.music.lover.hometask.exception.UriBuildException;
import com.music.lover.hometask.exception.error.ApplicationError;
import com.music.lover.hometask.service.AlbumService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/artists/{amgArtistId}/albums", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AlbumController implements BaseAuthenticatedController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping
    public List<AlbumResponse> getAlbums(
            @ModelAttribute("authenticationBeingUsed") AuthenticationInformation authenticationInformation,
            @PathVariable(name = "amgArtistId") Long amgArtistId
    ) {
        try {
            return albumService.getAlbums(amgArtistId);
        } catch (ServiceException e) {
            throw new RequestException(ApplicationError.SERVICE_UNAVAILABLE);
        } catch (UriBuildException e) {
            throw new RequestException(ApplicationError.SYS_ERR);
        }
    }

}
