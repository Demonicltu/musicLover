package com.music.lover.hometask.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class UserLoginResponse {

    private final String uuid;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public UserLoginResponse(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

}
