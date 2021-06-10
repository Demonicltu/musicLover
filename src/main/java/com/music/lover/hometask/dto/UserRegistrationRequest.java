package com.music.lover.hometask.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotBlank;

public class UserRegistrationRequest {

    private final String name;

    private final String username;

    private final String password;

    private final String repeatPassword;

    @JsonCreator
    public UserRegistrationRequest(
            @NotBlank String name,
            @NotBlank String username,
            @NotBlank String password,
            @NotBlank String repeatPassword
    ) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

}
