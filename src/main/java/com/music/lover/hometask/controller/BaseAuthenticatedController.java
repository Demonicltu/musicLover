package com.music.lover.hometask.controller;

import com.music.lover.hometask.dto.AuthenticationInformation;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

public interface BaseAuthenticatedController {

    @ModelAttribute("authenticationBeingUsed")
    default AuthenticationInformation sessionBeingUsed(HttpServletRequest httpServletRequest) {
        return (AuthenticationInformation) httpServletRequest.getAttribute("authentication");
    }

}
