package com.music.lover.hometask.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotBlank;

public class UserLoginRequest {

    private final String username;

    private final String password;

    @JsonCreator
    public UserLoginRequest(
            @NotBlank String username,
            @NotBlank String password
    ) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
