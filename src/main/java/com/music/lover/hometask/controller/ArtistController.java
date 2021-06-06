package com.music.lover.hometask.controller;


import com.music.lover.hometask.service.ArtistService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/artists")
public class ArtistController implements BaseAuthenticatedController {

    private final ArtistService artistService;

    public ArtistController(
            ArtistService artistService
    ) {
        this.artistService = artistService;
    }



}
